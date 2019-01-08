package com.asiainfo.fcm.controller;


import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IChannelEvalutionService;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/11/23.
 */
@RestController
@RequestMapping("/channelEvalution")
public class ChannelEvalutionController {
    private static Logger logger = Logger.getLogger(AreaEvalutionController.class);

    @Autowired
    private IChannelEvalutionService channelEvalutionService;

    /**
     *  获取效果评估 渠道评估信息
     *  @return List<Map> 集合
     * */
    @GetMapping("/getChannelReportInfo")
    public Result getChannelReportInfo(HttpServletRequest request)throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
            String date = request.getParameter("time").toString();
            String areaId = request.getParameter("areaId").toString();
            String channelId = request.getParameter("channelId").toString();
            String isFiltered = request.getParameter("filtered").toString();
            String page = request.getParameter("page").toString();

            int startRow = Integer.parseInt(page);
            startRow = startRow-1>0 ? (startRow-1)*10 : 0;
            Map<String ,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("date", date);
            paramMap.put("areaId", areaId);
            paramMap.put("channelId", channelId);
            paramMap.put("isFiltered", isFiltered);
            paramMap.put("startRow", startRow);

            //获取统计指标
            Map countIndexMap  = channelEvalutionService.getChannelCountIndex(paramMap);
            //获取列表数据
            List<Map<String,Object>> ChannelReportList = channelEvalutionService.getChannelEvalution(paramMap);
            //获取数据总数
            int totals = channelEvalutionService.getChannelEvalutionAllPage(paramMap);

            resultMap.put("countIndex", countIndexMap);
            resultMap.put("channelReportList", ChannelReportList);
            resultMap.put("totals", totals);

            return ResultUtil.success(resultMap);

    }

    /**
     *  获取效果评估 地市评估信息 数据下载
     *  @return List<Map> 集合
     * */
    @GetMapping("/getChannelReportDownload")
    public Result getChannelReportDownload(HttpServletRequest request,HttpServletResponse response)throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String date = request.getParameter("time").toString();
        String areaId = request.getParameter("areaId").toString();
        String channelId = request.getParameter("channelId").toString();
        String isFiltered = request.getParameter("filtered").toString();

        Map<String ,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("date", date);
        paramMap.put("areaId", areaId);
        paramMap.put("channelId", channelId);
        paramMap.put("isFiltered", isFiltered);

        String name = "渠道评估_";
        String sheetName=name+date;
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        //获取列表数据
        list = channelEvalutionService.getChannelEvalutionAll(paramMap);
        String [][] header={{"执行渠道","地市","任务数量","活动数量","目标用户数","接触量","响应量","营销成功量"}};
        String columns = "channel_name,city_name,task_num2,act_num,obj_num,touch_num,response_num,vic_num";
        exportComplexExcel(request,response,sheetName,header,columns,list);

        return ResultUtil.success();

    }


    public static <T> void exportComplexExcel(HttpServletRequest request,HttpServletResponse response, String sheetName, String[][] header,String columns, List<T> data) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexecl");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(sheetName.getBytes("gb2312"), "ISO8859-1") + ".xls");
        OutputStream os = response.getOutputStream();
        ExcelUtil excelImport = new ExcelUtil();
        excelImport.createBook(os);
        if (data != null && data.size() > 0 && !(data.get(0) instanceof Map))
            excelImport.writerSheetWithObject(sheetName, data,
                    columns.split(","), header);
        else
            excelImport.writerSheet(sheetName,
                    (List<Map<String, String>>) data, columns.toUpperCase()
                            .split(","), header);
        excelImport.closeBook();
        os.close();
    }
}
