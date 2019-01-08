package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Page;
import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.TaskEvalution;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.service.ITaskEvalutionService;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 任务评估
 * Created by ShaoJY on 2017/11/6.
 */
@RestController
@RequestMapping("/taskEvalution")
public class TaskEvalutionController {

    @Autowired
    private ITaskEvalutionService taskEvalutionService;

    @RequestMapping("/getTaskEvalutionList")
    public Result getTaskEvalutionList(@RequestParam("searchVal") String searchVal,
                                       @RequestParam("pageSize") String pageSize,
                                       @RequestParam("currentPage") String page,
                                       @RequestParam("startTime") String startTime,
                                       @RequestParam("endTime") String endTime,
                                       @RequestParam("cityId") String cityId) throws Exception{
        Map parameterMap = new HashMap();

        int lengthRow= Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(page);
        int startRow = (currentPage-1)*lengthRow;
        parameterMap.put("searchVal",searchVal);
        parameterMap.put("lengthRow",lengthRow);
        parameterMap.put("startRow",startRow);
        parameterMap.put("cityId",cityId);
        parameterMap.put("startTime",startTime);
        parameterMap.put("endTime",endTime);

        Map resultMap = new HashMap();
        List<TaskEvalution> taskEvalutionList = taskEvalutionService.getTaskEvalutionList(parameterMap);
        int totals = taskEvalutionService.getTaskEvalutionListTotals(parameterMap);

        resultMap.put("taskEvalutionList",taskEvalutionList);
       resultMap.put("totals",totals);
        return ResultUtil.success(resultMap);
    }


    //下载
    @GetMapping("/taskEvalutionListDownload")
    public Result getAllTaskEvalutionList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);
        String currentCity = user.getCityId();

        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String cityId = request.getParameter("cityId");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        Map parameterMap = new HashMap<>();

        parameterMap.put("searchVal", searchVal);
        parameterMap.put("cityId", cityId);
        parameterMap.put("startTime", startTime);
        parameterMap.put("endTime", endTime);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());
        String name = "任务评估列表_";
        String sheetName = name+nowTime;

        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        //获取列表数据
        list = taskEvalutionService.getAllTaskEvalutionList(parameterMap);

        String [][] header = {{"任务ID","任务名称","地市","创建时间","目标客户数","接触量","接触率","业务办理成功量","成功订购率","已关联活动个数"}};
        String columns = "task_id,task_name,city_name,op_time,target_num,touch_num,touch_rate,vic_num,vic_rate,related_num";
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

