package com.asiainfo.fcm.util;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.*;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author 
 * @date 
 */
public class  ExcelUtil {

	private WritableWorkbook book;

	private static final Logger LOG = Logger.getLogger(ExcelUtil.class);

	private int defaultFontSize = 11;

	public static int COUNT_VALUE=0;

	private boolean flag=true;

	private int headerSize;
	public void createBook(String excelName, String path) {
		if (book == null) {
			try {
				book = Workbook.createWorkbook(new File(path + "/" + excelName));
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("创建文件失败："+path + "/" + excelName+","+e.getMessage(), e);
			}
		}
	}

	public void createBook(File file) {
		if (book == null) {
			try {
				if (file.exists()) {
					Workbook book1 = Workbook.getWorkbook(file);
					book = Workbook.createWorkbook(file, book1);
				} else {
					book = Workbook.createWorkbook(file);
				}
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e.getMessage(),  e);
			} catch (BiffException e) {
				e.printStackTrace();
			}
		}
	}

	public void createBook(OutputStream os) {
		if (book == null) {
			try {
				book = Workbook.createWorkbook(os);
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public void closeBook() {
		if(book!=null){
			try {
				book.write();
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
			}

			try {
				book.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	public void writerSheet(String sheetName, List<Map<String,String>> data,String[] colsArray, String[][] header) {
		writerSheet(sheetName, data, colsArray,header, null, null,false);
	}
	public void writerSheet(String sheetName, List<Map<String,String>> data,String[] colsArray, String[][] header, boolean isAppend) {
		writerSheet(sheetName, data,colsArray, header, null, null,isAppend);
	}
	public void writerSheet(String sheetName, List<Map<String,String>> data, String[] colsArray,String[][] header, List<Map<String,Object>> colsProp) {
		writerSheet(sheetName, data,colsArray, header, colsProp, null,false);
	}
	public void writerSheet(String sheetName, List<Map<String,String>> data,String[] colsArray, String[][] header, List<Map<String,Object>> colsProp,boolean isAppend) {
		writerSheet(sheetName, data,colsArray, header, colsProp, null,isAppend);
	}
	/**
	 * 
	 * @param sheetName
	 * @param data 数据只支持double与string
	 * @param header 表头
	 */
	public void writerSheet(String sheetName, List<Map<String,String>> data,String[] colsArray, String[][] header, List<Map<String,Object>> colsProp, Map<String,Object> attach, boolean isAppend) {
		if(book!=null){
			if(sheetName==null || sheetName.length()==0){//处理sheet名字为空或重复
				sheetName = "Sheet"+(book.getSheetNames().length+1);
			}
			WritableSheet sheet = null;
			String[] existSheetNames = book.getSheetNames();
			for (int i = 0; i < existSheetNames.length; i++) {
				if(existSheetNames[i].equalsIgnoreCase(sheetName)){
					if(isAppend){
						sheet = book.getSheet(existSheetNames[i]);
					}else{
						sheetName+="(1)";
					}
					continue;
				}
			}
			if(sheet==null){
				sheet = book.createSheet(sheetName, book.getNumberOfSheets() + 1);
			}
			sheet.getSettings().setOrientation(PageOrientation.LANDSCAPE);
			/*由外部传入sheet.getSettings().setFitWidth(1);
			sheet.getSettings().setFitHeight(1);*/
			int rowOffset = 0;
			if(header!=null){
				Integer excelTitleFontSize = defaultFontSize;
				for (int k = 0; colsProp!=null && k < colsProp.size(); k++) {//取最大的字体传给表头
					Map<String,Object> colProp = colsProp.get(k);
					Integer titleFontSize = (Integer)colProp.get("titleFontSize");
					if(titleFontSize!=null && titleFontSize>excelTitleFontSize){
						excelTitleFontSize = titleFontSize; 
					}
				}
				rowOffset = addHeader(sheet, header,excelTitleFontSize);
				headerSize=header.length;
			}else{
				rowOffset = sheet.getRows();
				if(rowOffset>0){
					rowOffset+=2;
				}
			}
			int endRow = addBody01(sheet, data,colsArray, rowOffset,colsProp);

			for (int k = 0; colsProp!=null && k < colsProp.size(); k++) {//重新设置单元格宽度，因为在body中按照文字的长度设置的宽度
				Map<String,Object> colProp = colsProp.get(k);
				if(colProp!=null){
					int width = (Integer)colProp.get("width");
					if(width>0){
						try{
							int colWidth = width/defaultFontSize;
							if(colWidth>10){
								sheet.setColumnView(k, colWidth+2);
							}else{
								sheet.setColumnView(k, 12);
							}
						}catch(Exception e){}
					}

				}

			}
			int colSize =  -1;
			addAttach(sheet, attach, rowOffset, colSize);
		}else{
			LOG.error("book为空，构建sheet失败");
		}
	}

	private void addAttach(WritableSheet sheet, Map<String,Object> gridAttach, int rowOffset , int colSize){
		if(gridAttach!=null && gridAttach.size()>0){
			List<List<Map<String,Object>>> append = (List<List<Map<String,Object>>>)gridAttach.get("append");
			if(append!=null && append.size()>0){
				handleARow(sheet, append, rowOffset, colSize, false);
			}

			List<List<Map<String,Object>>> prepend = (List<List<Map<String,Object>>>)gridAttach.get("prepend");

			if(prepend!=null && prepend.size()>0){
				handleARow(sheet, prepend, rowOffset, colSize, ExcelUtil.COUNT_VALUE>0?true:false);
			}
		}
	}

	private void handleARow(WritableSheet sheet,List<List<Map<String,Object>>> attachItem,int rowStart, int colSize,boolean insertRow){
		for (int k = 0; k < attachItem.size(); k++) {
			if(insertRow){
				//LOG.info("insert before sheet's rows is"+sheet.getRows());
				sheet.insertRow(k+rowStart-headerSize);
				//LOG.info("insert after sheet's rows is"+sheet.getRows());
			}else{
				sheet.insertRow(k);
				//ExcelWriter.COUNT_VALUE++;
				flag=false;
			}

			List<Map<String,Object>> row = attachItem.get(k);
			try {
				for (int i = 0; row!=null && i < row.size(); i++) {
					Map<String,Object> col = row.get(i);

					int colIndex = -1;
					int colSpan = 1;
					try{
						colIndex = ((Integer)col.get("colIndex")).intValue();
						colSpan = ((Integer)col.get("colSpan")).intValue();
					}catch(Exception e){
						e.printStackTrace();
					}

					if(colSpan>1){
						sheet.mergeCells(colIndex,k+rowStart-headerSize,colIndex+colSpan-1,k+rowStart-headerSize);
					}

					Label cell;
					if(!flag){

						cell = new Label(colIndex,0,(String)col.get("text"));
					}else {
						cell = new Label(colIndex,k+rowStart-headerSize,(String)col.get("text"));
						//ExcelWriter.COUNT_VALUE--;
					}

					String textAlign = (String)col.get("textAlign");
					int fontSize = 10;
					try{
						fontSize = (Integer)col.get("fontSize");
					}catch(Exception e){
					}

					String fontWeight = (String)col.get("fontWeight");
					String colorStyle = (String)col.get("color");
					String background = (String)col.get("background");
					Color fontColor = null;
					Color bgColor = null;
					try{
						if(colorStyle!=null && colorStyle.length()>0){
							fontColor = Color.decode(colorStyle);
						}

						if(background!=null && background.length()>0){
							bgColor = Color.decode(background);
						}
					}catch(Exception e){}
					Colour excelFontColor = getNearestColour(fontColor);
					if(excelFontColor==null){
						excelFontColor = Colour.BLACK;
					}

					WritableFont wf = new WritableFont(WritableFont.ARIAL,fontSize,"bold".equalsIgnoreCase(fontWeight)?WritableFont.BOLD:WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,excelFontColor); 
					WritableCellFormat wcf = new WritableCellFormat(wf);
					try {
						if("left".equalsIgnoreCase(textAlign)||"right".equalsIgnoreCase(textAlign)){
							wcf.setAlignment("left".equalsIgnoreCase(textAlign)?Alignment.LEFT:Alignment.RIGHT);
						}else{
							wcf.setAlignment(Alignment.CENTRE);
						}
						/*Colour excelbgColor = getNearestColour(bgColor);
						if(excelFontColor!=null){
							wcf.setBackground(excelbgColor);
						}*/
						cell.setCellFormat(wcf);
						sheet.addCell(cell);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}

			} catch (WriteException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected static Colour getNearestColour(Color awtColor) {
		Colour color = null;
		if(awtColor!=null){
			Colour[] colors = Colour.getAllColours();
			if ((colors != null) && (colors.length > 0)) {
				Colour crtColor = null;
				int[] rgb = null;
				int diff = 0;
				int minDiff = 999;
				for (int i = 1; i < colors.length; i++) {
					crtColor = colors[i];
					rgb = new int[3];
					rgb[0] = crtColor.getDefaultRGB().getRed();
					rgb[1] = crtColor.getDefaultRGB().getGreen();
					rgb[2] = crtColor.getDefaultRGB().getBlue();
					diff = Math.abs(rgb[0] - awtColor.getRed()) + Math.abs(rgb[1] - awtColor.getGreen()) + Math.abs(rgb[2] - awtColor.getBlue());
					if (diff < minDiff) {
						minDiff = diff;
						color = crtColor;
					}
				}
			}
		}
		return color;
	}

	private int addHeader(WritableSheet sheet,String[][] header,Integer fontSize){
		int appendOffset = sheet.getRows();
		if(appendOffset>0){
			appendOffset+=2;
		}
		int i = 0;
		WritableFont wf = new WritableFont(WritableFont.ARIAL,fontSize!=null&&fontSize>5?fontSize:defaultFontSize,WritableFont.BOLD,false); 
		WritableCellFormat wcf = new WritableCellFormat(wf);
		try {
			wcf.setAlignment(Alignment.CENTRE);
			wcf.setBackground(Colour.GRAY_25);
			wcf.setBorder(Border.ALL,BorderLineStyle.THIN);
		} catch (WriteException e1) {
			e1.printStackTrace();
		}

		for (; i < header.length; i++) {
			String[] cols = header[i];
			for (int j = 0; j < cols.length; j++) {
				try {
					String cellLabel = cols[j];
					//判断是否需要合并，优先行合并
					if(i>0 && cellLabel.length()>0 && cellLabel.equalsIgnoreCase(header[i-1][j]) ){//合并列

						//判断是不是中间位置，同下比较，相等就pass
						if (i + 1 < header.length && cellLabel.equalsIgnoreCase(header[i+1][j])) {
							continue;
						}

						// 再判断第一个rowspan的位置
						int rowOffset = i;
						while(rowOffset>0 && cellLabel.equalsIgnoreCase(header[rowOffset-1][j])){
							rowOffset--;
						}
						Range range = sheet.mergeCells(j, rowOffset+appendOffset, j, i+appendOffset);
						if (range.getTopLeft() instanceof Label) {
							Label cell = (Label) range.getTopLeft();
							cell.setCellFormat(wcf);
						}

					}else if(j>0 && cellLabel.length()>0 && cellLabel.equalsIgnoreCase(header[i][j-1])){//合并行
						//判断是不是中间位置，同下比较，相等就pass
						if (j + 1 < cols.length && cellLabel.equalsIgnoreCase(header[i][j+1])) {
							continue;
						}
						//再判断第一个colspan的位置
						int colOffset = j;
						while(colOffset>0 && cellLabel.equalsIgnoreCase(header[i][colOffset-1])){
							colOffset--;
						}
						Range range = sheet.mergeCells(colOffset, i+appendOffset, j, i+appendOffset);
						if (range.getTopLeft() instanceof Label) {
							Label cell = (Label) range.getTopLeft();
							cell.setCellFormat(wcf);
						}
					}else{
						Label cell = new Label(j, i+appendOffset, cellLabel);
						cell.setCellFormat(wcf);
						sheet.addCell(cell);
					}
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
		}
		return i+appendOffset;
	}

	int maxCellWidth = 30;
	private int addBody01(WritableSheet sheet, List<Map<String,String>> data,String[] colsArray,int rowOffset,List<Map<String,Object>> colsProp){
		int endRow = rowOffset;
		int i = 0;
		int[] colMaxLength = null;
		int colSize = 0;
		for (; i < data.size() && data.size()<65000; i++) {
			Map<String,String> line = data.get(i);
			if(i==0){
				colSize = line.size();
				colMaxLength = new int[colSize];
			}
			for (int j = 0; j < colsArray.length; j++) {
				try{
					Object obj = line.get(colsArray[j]);
					WritableCell cell = null;
					int curLength = 0;
					if(obj!=null && isNumeric(obj.getClass().getName())){
						if((null!=colsArray[j]&&colsArray[j]!="")&&(colsArray[j].endsWith("HB")||colsArray[j].endsWith("hb")||colsArray[j].endsWith("TB")||colsArray[j].endsWith("tb"))){
							if (obj instanceof Double) {
								cell = new Label(j, i+rowOffset, obj!=null?obj.toString()+"%":"");
							}else {							
								cell = new Label(j, i+rowOffset, obj!=null?obj.toString()+"%":"");
							}
						}else {
							if (obj instanceof Double) {
								cell = new Number(j, i+rowOffset, (Double)obj);
							}else {							
								cell = new Number(j, i+rowOffset, Double.parseDouble(obj.toString()));
							}
						}

						curLength = cell.getContents().length();
					}else{
						cell = new Label(j, i+rowOffset, obj!=null?obj.toString():"");						
						curLength = cell.getContents().length()*2;
					}
					if(j<colMaxLength.length&&colMaxLength[j]<curLength){
						colMaxLength[j] = curLength;
					}

					Integer fontSize = null;
					String align = null;
					if(colsProp!=null&&colsProp.get(j)!=null){
						Map<String,Object> colProp = colsProp.get(j);
						fontSize = (Integer)colProp.get("fontSize");
						align = (String)colProp.get("align");
					}

					int excelFontSize = defaultFontSize;
					if(fontSize!=null && fontSize>5){
						excelFontSize = fontSize;
					}
					WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.ARIAL,excelFontSize,WritableFont.NO_BOLD,false));
					wcf.setBorder(Border.ALL,BorderLineStyle.THIN);

					if("left".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.LEFT);
					}else if("right".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.RIGHT);
					}else if("center".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.CENTRE);
					}

					wcf.setWrap(true);
					cell.setCellFormat(wcf);
					sheet.addCell(cell);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		for (int k = 0; k < colSize; k++) {
			if(colMaxLength!=null && colMaxLength[k]>12){
				sheet.setColumnView(k, colMaxLength[k]>maxCellWidth?maxCellWidth:colMaxLength[k]);
			}else{
				sheet.setColumnView(k, 12);
			}
		}
		return endRow+i;
	}

	private int addBody(WritableSheet sheet, List<List<Object>> data,int rowOffset,List<Map<String,Object>> colsProp){
		int endRow = rowOffset;
		int i = 0;
		int[] colMaxLength = null;
		int colSize = 0;
		for (; i < data.size() && data.size()<65000; i++) {
			List<Object> line = data.get(i);
			if(i==0){
				colSize = line.size();
				colMaxLength = new int[colSize];
			}
			for (int j = 0; j < line.size(); j++) {
				try{
					Object obj = line.get(j);
					WritableCell cell = null;
					int curLength = 0;
					if(obj!=null && isNumeric(obj.getClass().getName())){
						if (obj instanceof Double) {
							cell = new Number(j, i+rowOffset, (Double)obj);
						}else {							
							cell = new Number(j, i+rowOffset, Double.parseDouble(obj.toString()));
						}
						curLength = cell.getContents().length();
					}else{
						cell = new Label(j, i+rowOffset, obj!=null?obj.toString():"");						
						curLength = cell.getContents().length()*2;
					}
					if(colMaxLength[j]<curLength){
						colMaxLength[j] = curLength;
					}

					Integer fontSize = null;
					String align = null;
					if(colsProp!=null&&colsProp.get(j)!=null){
						Map<String,Object> colProp = colsProp.get(j);
						fontSize = (Integer)colProp.get("fontSize");
						align = (String)colProp.get("align");
					}

					int excelFontSize = defaultFontSize;
					if(fontSize!=null && fontSize>5){
						excelFontSize = fontSize;
					}
					WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.ARIAL,excelFontSize,WritableFont.NO_BOLD,false));
					wcf.setBorder(Border.ALL,BorderLineStyle.THIN);

					if("left".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.LEFT);
					}else if("right".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.RIGHT);
					}else if("center".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.CENTRE);
					}

					wcf.setWrap(true);
					cell.setCellFormat(wcf);
					sheet.addCell(cell);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		for (int k = 0; k < colSize; k++) {
			if(colMaxLength!=null && colMaxLength[k]>12){
				sheet.setColumnView(k, colMaxLength[k]>maxCellWidth?maxCellWidth:colMaxLength[k]);
			}else{
				sheet.setColumnView(k, 12);
			}
		}
		return endRow+i;
	}

	private boolean isNumeric(String className){
		return "java.lang.Integer".equals(className) || "java.lang.Long".equals(className) || 
				"java.lang.Double".equals(className)||"java.lang.Float".equals(className) ||
				"java.math.BigDecimal".equals(className);
	}

	/**
	 * data <string[]>
	 * 
	 * @param data
	 * @param sheetName
	 */
	public void writerSheet(String sheetName,List<Map<String,String>> data ) {
		writerSheet(sheetName, data,null, null,null,false);
	}
	public <T> WritableSheet writerSheetWithObject(String sheetName, List<T> data,String[] colsArray, String[][] header) {
		return writerSheetWithObject(sheetName,data, colsArray,header, null, null,false);

	}
	public WritableWorkbook  writerHeaderWithObject(String sheetName, String[][] header, List<Map<String,Object>> colsProp) {
		return writerHeaderWithObject(sheetName,header,colsProp,false);
	}
	public WritableWorkbook  writerHeaderWithObject(String sheetName, String[][] header) {
		return writerHeaderWithObject(sheetName,header,null,false);
	}
	public WritableWorkbook  writerHeaderWithObject(String sheetName, String[][] header, List<Map<String,Object>> colsProp, boolean isAppend) {
		if(book!=null){
			if(sheetName==null || sheetName.length()==0){//处理sheet名字为空或重复
				sheetName = "Sheet"+(book.getSheetNames().length+1);
			}
			WritableSheet sheet = getWritableSheet(sheetName,isAppend);
			int rowOffset = 0;
			if(header!=null){
				Integer excelTitleFontSize = defaultFontSize;
				for (int k = 0; colsProp!=null && k < colsProp.size(); k++) {//取最大的字体传给表头
					Map<String,Object> colProp = colsProp.get(k);
					Integer titleFontSize = (Integer)colProp.get("titleFontSize");
					if(titleFontSize!=null && titleFontSize>excelTitleFontSize){
						excelTitleFontSize = titleFontSize; 
					}
				}
				rowOffset = addHeader(sheet, header,excelTitleFontSize);
				headerSize=header.length;
			}else{
				rowOffset = sheet.getRows();
				if(rowOffset>0){
					rowOffset+=2;
				}
			}
			return book;
		}else{
			LOG.error("book为空，构建sheet失败");
		}
		return null;
	}
	public WritableSheet getWritableSheet(String sheetName, boolean isAppend){
		WritableSheet sheet = null;
		String[] existSheetNames = book.getSheetNames();
		for (int i = 0; i < existSheetNames.length; i++) {
			if(existSheetNames[i].equalsIgnoreCase(sheetName)){
				if(isAppend){
					sheet = book.getSheet(existSheetNames[i]);
				}else{
					sheetName+="(1)";
				}
				continue;
			}
		}
		if(sheet==null){
			sheet = book.createSheet(sheetName, book.getNumberOfSheets() + 1);
		}
		sheet.getSettings().setOrientation(PageOrientation.LANDSCAPE);
		return sheet;
	}
	private <T> int addBodyWithObject(WritableSheet sheet, List<T> data,String[] colsArray,int rowOffset,List<Map<String,Object>> colsProp){
		int endRow = rowOffset;
		int i = 0;
		int[] colMaxLength = null;
		int colSize = 0;
		colSize = sheet.getColumns();
		colMaxLength = new int[colSize];
		Class clazz=null;
		if(data.size()>0){
			clazz=data.get(0).getClass();
		}
		for (; i < data.size() && data.size()<65000; i++) {
			T line = data.get(i);
			for (int j = 0; j < colsArray.length; j++) {
				try{
					Object obj=null;
					if(null!=colsArray[j]&&colsArray[j].length()>0)
					obj= clazz.getDeclaredMethod("get"+colsArray[j].substring(0, 1).toUpperCase()+
							colsArray[j].substring(1)).invoke(line);
					WritableCell cell = null;
					int curLength = 0;
					if(obj!=null && isNumeric(obj.getClass().getName())){
						if((null!=colsArray[j]&&colsArray[j]!="")&&(colsArray[j].endsWith("HB")||colsArray[j].endsWith("hb")||colsArray[j].endsWith("TB")||colsArray[j].endsWith("tb"))){
							if (obj instanceof Double) {
								cell = new Label(j, i+rowOffset, obj!=null?obj.toString()+"%":"");
							}else {							
								cell = new Label(j, i+rowOffset, obj!=null?obj.toString()+"%":"");
							}
						}else {
							if (obj instanceof Double) {
								cell = new Number(j, i+rowOffset, (Double)obj);
							}else {							
								cell = new Number(j, i+rowOffset, Double.parseDouble(obj.toString()));
							}
						}

						curLength = cell.getContents().length();
					}else{
						cell = new Label(j, i+rowOffset, obj!=null?obj.toString():"");						
						curLength = cell.getContents().length()*2;
					}
					if(j<colMaxLength.length&&colMaxLength[j]<curLength){
						colMaxLength[j] = curLength;
					}

					Integer fontSize = null;
					String align = null;
					if(colsProp!=null&&colsProp.get(j)!=null){
						Map<String,Object> colProp = colsProp.get(j);
						fontSize = (Integer)colProp.get("fontSize");
						align = (String)colProp.get("align");
					}

					int excelFontSize = defaultFontSize;
					if(fontSize!=null && fontSize>5){
						excelFontSize = fontSize;
					}
					WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.ARIAL,excelFontSize,WritableFont.NO_BOLD,false));
					wcf.setBorder(Border.ALL,BorderLineStyle.THIN);

					if("left".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.LEFT);
					}else if("right".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.RIGHT);
					}else if("center".equalsIgnoreCase(align)){
						wcf.setAlignment(Alignment.CENTRE);
					}
					wcf.setWrap(true);
					cell.setCellFormat(wcf);
					sheet.addCell(cell);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		for (int k = 0; k < colSize; k++) {
			if(colMaxLength!=null && colMaxLength[k]>12){
				sheet.setColumnView(k, colMaxLength[k]>maxCellWidth?maxCellWidth:colMaxLength[k]);
			}else{
				sheet.setColumnView(k, 12);
			}
		}
		return endRow+i;
	}
	public <T> WritableSheet writerSheetWithObject(String sheetName, List<T> data,String[] colsArray, String[][] header, List<Map<String,Object>> colsProp, Map<String,Object> attach, boolean isAppend) {
		if(book!=null){
			if(sheetName==null || sheetName.length()==0){//处理sheet名字为空或重复
				sheetName = "Sheet"+(book.getSheetNames().length+1);
			}
			WritableSheet sheet = getWritableSheet(sheetName,isAppend);
			int rowOffset = 0;
			if(header!=null){
				Integer excelTitleFontSize = defaultFontSize;
				for (int k = 0; colsProp!=null && k < colsProp.size(); k++) {//取最大的字体传给表头
					Map<String,Object> colProp = colsProp.get(k);
					Integer titleFontSize = (Integer)colProp.get("titleFontSize");
					if(titleFontSize!=null && titleFontSize>excelTitleFontSize){
						excelTitleFontSize = titleFontSize; 
					}
				}
				rowOffset = addHeader(sheet, header,excelTitleFontSize);
				headerSize=header.length;
			}else{
				rowOffset = sheet.getRows();
				if(rowOffset>0){
					rowOffset+=2;
				}
			}
			int endRow = addBodyWithObject(sheet, data,colsArray, rowOffset,colsProp);

			for (int k = 0; colsProp!=null && k < colsProp.size(); k++) {//重新设置单元格宽度，因为在body中按照文字的长度设置的宽度
				Map<String,Object> colProp = colsProp.get(k);
				if(colProp!=null){
					int width = (Integer)colProp.get("width");
					if(width>0){
						try{
							int colWidth = width/defaultFontSize;
							if(colWidth>10){
								sheet.setColumnView(k, colWidth+2);
							}else{
								sheet.setColumnView(k, 12);
							}
						}catch(Exception e){}
					}

				}

			}
			int colSize =  -1;
			addAttach(sheet, attach, rowOffset, colSize);
			return sheet;
		}else{
			LOG.error("book为空，构建sheet失败");
		}
		return null;
	}
	public int appendSheetRow(WritableSheet sheet,int row,Object [] rowData){
		try{
			if(null!=rowData&&rowData.length>0){
				WritableCellFormat wcf = new WritableCellFormat(new WritableFont(WritableFont.ARIAL,defaultFontSize,WritableFont.NO_BOLD,false));
				wcf.setBorder(Border.ALL,BorderLineStyle.THIN);
				wcf.setAlignment(Alignment.CENTRE);
				wcf.setWrap(true);
				int writeRow=row-1;
				int length=rowData.length;
				for (int i=0;i<length;i++) {
					Object object= rowData[i];

					WritableCell cell = null;
					if(null!=object){
						if(isNumeric(object.getClass().getName())){						
									cell = new Number(i,writeRow, Double.parseDouble(object.toString()));
						}
						else{
							cell= new Label(i,writeRow,object.toString());
						}
					}
					else{
						cell= new Label(i,writeRow,null);
					}
					cell.setCellFormat(wcf);
					sheet.addCell(cell);
				}
				if(length<sheet.getColumns()){
					int columNum=sheet.getColumns();

					
					for(int i=length;i<columNum;i++){
						WritableCell cell = null;
						cell= new Label(i,writeRow,null);
						cell.setCellFormat(wcf);
						sheet.addCell(cell);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	public List readExcel(InputStream is){
		List list= new ArrayList();
		try {
			Workbook wb=Workbook.getWorkbook(is);
			Sheet sheet=wb.getSheet(0);
			if(sheet.getRows()-1>0){
				for(int i=1;i<sheet.getRows();i++){
					Cell[] cells=sheet.getRow(i);
					String[] tmp=new String[cells.length];
					for(int j=1;j<cells.length;j++){
						tmp[j]=cells[j].getContents();
					}
					list.add(tmp);
				}
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		return list;
	}
}
