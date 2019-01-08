package com.asiainfo.fcm.util;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by PuMg on 2018/4/27/0027.
 */
public class ExcelExportUtil {

    /**
     * 数据行标识
     */
    public final static String DATA_LINE = "datas";
    /**
     * 默认样式标识
     */
    public final static String DEFAULT_STYLE = "defaultStyles";
    /**
     * 行样式标识
     */
    public final static String STYLE = "styles";
    /**
     * 插入序号样式标识
     */
    public final static String SER_NUM = "sernums";
    private static ExcelExportUtil et = new ExcelExportUtil();

    private Workbook wb;

    private Sheet sheet;
    /**
     * 数据的初始化列数
     */
    private int initColIndex;
    /**
     * 数据的初始化行数
     */
    private int initRowIndex;
    /**
     * 当前列数
     */
    private int curColIndex;
    /**
     * 当前行数
     */
    private int curRowIndex;
    /**
     * 当前行对象
     */
    private Row curRow;
    /**
     * 最后一行的数据
     */
    private int lastRowIndex;
    /**
     * 默认样式
     */
    private CellStyle defaultStyle;
    /**
     * 默认行高
     */
    private float rowHeight;
    /**
     * 存储某一方所对于的样式
     */
    private Map<Integer, CellStyle> styles;
    /**
     * 序号的列
     */
    private int serColIndex;

    private ExcelExportUtil() {

    }

    public static ExcelExportUtil getInstance() {
        return et;
    }


    /**
     * 1、读取相应的模板文档
     */
    public ExcelExportUtil readTemplateByClasspath(String path) {
        try {
            wb = WorkbookFactory.create(ExcelExportUtil.class.getClassLoader().getResourceAsStream(path));
            initTemplate();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("InvalidFormatException, please check.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("The template is not exist, please check.");
        }
        return this;
    }

    public ExcelExportUtil readTemplateByPath(String path) {
        try {
            wb = WorkbookFactory.create(new File(path));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("InvalidFormatException, please check.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("The template is not exist, please check.");
        }
        return this;
    }

    private void initTemplate() {
        sheet = wb.getSheetAt(0);
        initConfigData();
        lastRowIndex = sheet.getLastRowNum();
        curRow = sheet.getRow(curRowIndex);
    }

    /**
     * 循环遍历，找到有datas字符的那个单元，记录initColIndex，initRowIndex，curColIndex，curRowIndex
     * 调用initStyles()方法
     * 在寻找datas字符的时候会顺便找一下sernums，如果有则记录其列号serColIndex；如果没有则调用initSer()方法，重新循环查找
     */
    private void initConfigData() {
        boolean findData = false;
        boolean findSer = false;
        for (Row row : sheet) {
            if (findData) break;
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(SER_NUM)) {
                    serColIndex = c.getColumnIndex();
                    findSer = true;
                }
                if (str.equals(DATA_LINE)) {
                    initColIndex = c.getColumnIndex();
                    initRowIndex = row.getRowNum();
                    curColIndex = initColIndex;
                    curRowIndex = initRowIndex;
                    findData = true;
                    break;
                }
            }
        }
        if (!findSer) {
            initSer();
        }
        initStyles();
    }

    /**
     * 初始化序号位置
     */
    private void initSer() {
        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(SER_NUM)) {
                    serColIndex = c.getColumnIndex();
                }
            }
        }
    }

    /**
     * 初始化样式信息
     */
    private void initStyles() {
        styles = new HashMap<Integer, CellStyle>();
        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(DEFAULT_STYLE)) {
                    defaultStyle = c.getCellStyle();
                    rowHeight = row.getHeightInPoints();
                }
                if (str.equals(STYLE) || str.equals(SER_NUM)) {
                    styles.put(c.getColumnIndex(), c.getCellStyle());
                }
            }
        }
    }

    public void writeToFile(String filepath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            wb.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("写入的文件不存在" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入数据失败" + e.getMessage());
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void writeToStream(OutputStream stream) {
        try {
            wb.write(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("写入的文件不存在" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入数据失败" + e.getMessage());
        } finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void createCell(String value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue(value);
        curColIndex++;
    }

    public void createCell(int value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue((int) value);
        curColIndex++;
    }

    public void createCell(Date value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue(value);
        curColIndex++;
    }

    public void createCell(double value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue(value);
        curColIndex++;
    }

    public void createCell(boolean value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue(value);
        curColIndex++;
    }

    public void createCell(Calendar value) {
        Cell c = curRow.createCell(curColIndex);
        setCellStyle(c);
        c.setCellValue(value);
        curColIndex++;
    }

    /**
     * 设置某个单元格的样式
     *
     * @param c
     */
    private void setCellStyle(Cell c) {
        if (styles.containsKey(c.getColumnIndex())) {
            c.setCellStyle(styles.get(c.getColumnIndex()));
        } else {
            c.setCellStyle(defaultStyle);
        }
    }

    public void clearDefaultStyle() {
        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.equals(DEFAULT_STYLE)) {
                    c.setCellStyle(null);
                    c.setCellValue("");
                }

            }
        }
    }

    public void createNewRow() {
        if (lastRowIndex > curRowIndex && curRowIndex != initRowIndex) {
            sheet.shiftRows(curRowIndex, lastRowIndex, 1, true, true);
            lastRowIndex++;
        }
        curRow = sheet.createRow(curRowIndex);
        curRow.setHeightInPoints(rowHeight);
        curRowIndex++;
        curColIndex = initColIndex;
    }

    /**
     * 插入序号，会自动找相应的序号标示的位置完成插入
     */
    public void insertSer() {
        int index = 1;
        Row row = null;
        Cell c = null;
        for (int i = initRowIndex; i < curRowIndex; i++) {
            row = sheet.getRow(i);
            c = row.createCell(serColIndex);
            setCellStyle(c);
            c.setCellValue(index++);
        }
    }

    /**
     * 根据map替换相应的常量，通过Map中的值来替换#开头的值
     *
     * @param datas
     */
    public void replaceFinalData(Map<String, String> datas) {
        if (datas == null) return;
        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType() != Cell.CELL_TYPE_STRING) continue;
                String str = c.getStringCellValue().trim();
                if (str.startsWith("#")) {
                    if (datas.containsKey(str.substring(1))) {
                        c.setCellValue(datas.get(str.substring(1)));
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String filePath = "C:\\Users\\doyoulikeme\\Desktop\\redSeaReport.xlsx";
        FileInputStream inStream = new FileInputStream(new File(filePath));
        org.apache.poi.ss.usermodel.Workbook workBook = WorkbookFactory.create(inStream);
        //直接获取第一页
        org.apache.poi.ss.usermodel.Sheet sheet = workBook.getSheetAt(0);

        //该EXCEL的最后一行
        int LastRowNum = sheet.getLastRowNum();

        //获取当前EXCEL的第四行
        org.apache.poi.ss.usermodel.Row rownumbigger = sheet.getRow(3);
        //获取该行最后一列有多少列
        int LastCellNum = rownumbigger.getLastCellNum();

        //获取当前是第几行
        int num = rownumbigger.getRowNum();
        //获取当前是第几列
        org.apache.poi.ss.usermodel.Cell cell = rownumbigger.getCell(3);
        int a = cell.getColumnIndex();

        //获取当前sheet页有多少合并单元格
        int sheetMergeCount = sheet.getNumMergedRegions();
        List<String> listTittle  =   new  ArrayList<String>();
        int countRow = 0;
        int secondColumn=0;

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            String cellValue ="";
            String ScellValue ="";
            String TcellValue ="";
            String FcellValue="";
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();


            if (num >= firstRow && num <= lastRow) {
                if (a >= firstColumn && a <= lastColumn) {
                    secondColumn=  lastColumn+1;
                    org.apache.poi.ss.usermodel.Row fRow = sheet.getRow(firstRow);
                    org.apache.poi.ss.usermodel.Cell fCell = fRow.getCell(firstColumn);
                    if (fCell.getCellType() == fCell.CELL_TYPE_STRING) {
                        cellValue = fCell.getStringCellValue();
                    }
//							if (i==countRow ){
                    String oneValue="";
                    for(int l = 3;l<lastColumn+1;l++){
                        int sencondRow = firstRow+1;
                        if(firstColumn >= firstColumn &&firstColumn <= lastColumn) {
                            org.apache.poi.ss.usermodel.Row SRow = sheet.getRow((sencondRow));
                            org.apache.poi.ss.usermodel.Cell SCell = SRow.getCell(l);
                            ScellValue = SCell.getStringCellValue();
                            if (!ScellValue.equals("")){
                                oneValue = ScellValue;
                            }else{
                                ScellValue=oneValue;
                            }
                            int ThrRow = firstRow+3;
                            org.apache.poi.ss.usermodel.Row TRow = sheet.getRow(ThrRow);
                            org.apache.poi.ss.usermodel.Cell TCell = TRow.getCell(l);
                            if (TCell.getCellType() == TCell.CELL_TYPE_STRING) {
                                TcellValue = TCell.getStringCellValue();
                                String ALLValue = cellValue+ScellValue+TcellValue;
                                System.out.println("标题单元格为：" + ALLValue + ";   ");
                                listTittle.add(ALLValue);
                            }
                        }
                    }
//							}
                }if (secondColumn >= firstColumn && secondColumn <= lastColumn && secondColumn >=a){
                    org.apache.poi.ss.usermodel.Row fRow = sheet.getRow(firstRow);
                    org.apache.poi.ss.usermodel.Cell fCell = fRow.getCell(firstColumn);
                    if (fCell.getCellType() == fCell.CELL_TYPE_STRING) {
                        cellValue = fCell.getStringCellValue();
                    }
//							if (i==countRow+i ){
                    String oneValue="";
                    String twoValue="";
                    for(int sl = secondColumn;sl<lastColumn+1;sl++){
                        int sencondRow = firstRow+1;
                        if(secondColumn >= firstColumn &&secondColumn <= lastColumn) {
                            org.apache.poi.ss.usermodel.Row SRow = sheet.getRow((sencondRow));
                            org.apache.poi.ss.usermodel.Cell SCell = SRow.getCell(sl);
                            ScellValue = SCell.getStringCellValue();
                            if (!ScellValue.equals("")){
                                oneValue = ScellValue;
                            }else{
                                ScellValue=oneValue;
                            }
                            int ThrRow = firstRow+2;
                            org.apache.poi.ss.usermodel.Row TRow = sheet.getRow(ThrRow);
                            org.apache.poi.ss.usermodel.Cell TCell = TRow.getCell(sl);
                            TcellValue = TCell.getStringCellValue();
                            if (!TcellValue.equals("")){
                                twoValue = TcellValue;
                            }else{
                                TcellValue=twoValue;
                            }
                        }
                        int FourRow = firstRow+3;
                        org.apache.poi.ss.usermodel.Row FRow = sheet.getRow(FourRow);
                        org.apache.poi.ss.usermodel.Cell FCell = FRow.getCell(sl);
                        if (FCell.getCellType() == FCell.CELL_TYPE_STRING) {
                            FcellValue = FCell.getStringCellValue();
                            String ALLValue = cellValue+ScellValue+TcellValue+FcellValue;
                            System.out.println("标题单元格为：" + ALLValue + ";   ");
                            listTittle.add(ALLValue);
                        }
                    }
                }
            }
        }
        listTittle.add("端口");
    }
}

