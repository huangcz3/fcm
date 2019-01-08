package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.service.IActivityEvalutionService;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.ResultUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activityEvalution")
public class ActivityEvalutionController {

    private static Logger logger = Logger.getLogger(ActivityEvalutionController.class);

    @Autowired
    private IActivityEvalutionService activityEvalutionService;

    @GetMapping("/getActEvaList")
    public Result getActEvaList(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String searchVal = request.getParameter("searchTxt").toString();
        String areaId = request.getParameter("areaId").toString();
        String startTime = request.getParameter("stime").toString();
        String endTime = request.getParameter("endtime").toString();
        String channelId = request.getParameter("channel").toString();
        String page = request.getParameter("page").toString();
        String indexItem = request.getParameter("tagStr").toString();

        int startRow = Integer.parseInt(page);
        startRow = startRow - 1 > 0 ? (startRow - 1) * 10 : 0;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("searchVal", searchVal);
        paramMap.put("areaId", areaId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("channelId", channelId);
        paramMap.put("startRow", startRow);
        paramMap.put("indexItem", indexItem);

        List<Map<String, Object>> actEvaList = activityEvalutionService.getActEvalution(paramMap);
        //获取总页数
        int totals = activityEvalutionService.getActEvalutionTotals(paramMap);

        resultMap.put("actEvaList", actEvaList);
        resultMap.put("totals", totals);

        return ResultUtil.success(resultMap);

    }


    @RequestMapping("/getActivitiesDownload")
    @ResponseBody
    public Result getActivitiesDownload(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String searchVal = request.getParameter("searchTxt").toString();
        String areaId = request.getParameter("areaId").toString();
        String startTime = request.getParameter("stime").toString();
        String endTime = request.getParameter("endtime").toString();
        String channelId = request.getParameter("channel").toString();
        String indexItem = request.getParameter("tagStr").toString();
        String tagNameStr = request.getParameter("tagNameStr").toString();

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("searchVal", searchVal);
        paramMap.put("areaId", areaId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("channelId", channelId);
        paramMap.put("indexItem", indexItem);

        String name = "活动评估_";
        String sheetName = name + startTime + "-" + endTime;
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //获取列表数据
        list = activityEvalutionService.getActEvalutionAll(paramMap);

        String head = "活动编号,活动名称,地市,执行渠道,创建人,创建时间,开始时间,结束时间," + tagNameStr;
        String[] indexNames = head.split(",");
        String[][] temp = new String[indexNames.length][indexNames.length];
        for (int i = 0; i < indexNames.length; i++) {
            temp[0][i] = indexNames[i];
        }
        String[][] header = {temp[0]};
        String columns = "camp_id,camp_name,city_name,channel_name,create_name,create_time,start_time,end_time," + indexItem;

        exportComplexExcel(request, response, sheetName, header, columns, list);

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

    /**
     * 活动评估 明细数据 折线图
     * @param activityId
     * @param indexs
     * @return
     */
    @GetMapping("/getActEvalutionDetail")
    public Result getActEvalutionDetail(@RequestParam String activityId,@RequestParam String indexs)throws Exception{
        Map resultMap = activityEvalutionService.getActEvalutionDetail(activityId,indexs);
        return ResultUtil.success(resultMap);
    }


}
