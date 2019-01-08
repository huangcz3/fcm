package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.service.IBaseInitService;
import com.asiainfo.fcm.service.ITaskService;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.IDUtil;
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
 * Created by RUOK on 2017/7/5.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IBaseInitService baseInitService;

    /**
     * 获取地市列表
     */
    @GetMapping("/getCityList")
    public Result getCityList() throws Exception {
        String cityId = "1";
        List<City> cityList = baseInitService.getCityList(cityId);
        return ResultUtil.success(cityList);
    }

    @GetMapping("/getActivity4Task")
    public Result getActivity4Task(HttpSession session, @RequestParam(name = "keyword", defaultValue = "") String keyword, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        map.put("keyword", keyword);

        User user = UserUtil.getCurrentUser(session);
        map.put("cityId", user.getCityId());

        long totalRecord = taskService.getActivityTotalRecord4Task(map);

        Page page = new Page(currentPage, pageSize, totalRecord);
        map.put("page", page);

        List<Activity> activityList = taskService.getActivity4Task(map);

        resultMap.put("activityList", activityList);
        resultMap.put("totalRecord", totalRecord);
        return ResultUtil.success(resultMap);
    }

    @PostMapping("/add")
    public Result addTask(HttpSession session, @RequestBody Task task) {
        User user = UserUtil.getCurrentUser(session);
        task.setCreatorId(user.getUserId());
        task.setCreatorName(user.getUserName());
        task.setCityId(user.getCityId());
        task.setCityName(user.getCityName());

        String taskId = IDUtil.generateTaskId();
        task.setTaskId(taskId);

        if (task.getActivityList().size() > 0) {
            Task tmpTask = taskService.getTaskStartAndEndTime(task.getActivityList());

            task.setStartTime(tmpTask.getStartTime());
            task.setEndTime(tmpTask.getEndTime());
        }

        taskService.addTask(task);
        return ResultUtil.success();
    }

    @GetMapping("/getAllTasks")
    public Result getTasks(HttpSession session, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("keyword") String taskName, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize, @RequestParam("cityId") String cityId) {
        Task taskMap = new Task();
        taskMap.setStartTime(startTime);
        taskMap.setEndTime(endTime);
        taskMap.setTaskName(taskName);
        taskMap.setCityId(cityId);

        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        //taskMap.setCityId(user.getCityId());

        Map<String, Object> map = new HashMap<>();
        map.put("task", taskMap);

        long totalRecord = taskService.getTasksTotalRecord(map);

        Page page = new Page(currentPage, pageSize, totalRecord);
        map.put("page", page);
        map.put("userId", userId);
        List<Task> taskList = taskService.getTasks(map);

        page.setDataList(taskList);

        return ResultUtil.success(page);
    }

    @PostMapping("/delete")
    public Result deleteTask(@RequestParam("taskId") String taskId) {
        taskService.deleteTask(taskId);
        return ResultUtil.success();
    }

    @GetMapping("/getTaskDetail")
    public Result getTaskDetail(@RequestParam String taskId) {
        Map resultMap = new HashMap();
        Task taskDetail = taskService.getTaskDetail(taskId);
        List<TaskActivityRel> relatedAct = taskService.getRelativeActivities(taskId);
        resultMap.put("taskDetail", taskDetail);
        resultMap.put("relativeActs", relatedAct);
        return ResultUtil.success(resultMap);
    }

    //修改
    @PostMapping("/edit")
    public Result editTask(@RequestBody Task task,String taskId) {

        task.setTaskId(taskId);
        if (task.getActivityList().size() > 0) {
            Task tmpTask = taskService.getTaskStartAndEndTime(task.getActivityList());
            task.setStartTime(tmpTask.getStartTime());
            task.setEndTime(tmpTask.getEndTime());
        }

        taskService.editTask(task);
        return ResultUtil.success();
    }

    //下载
    @GetMapping("/getTaskListDownload")
    public Result getTaskListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
        String name = "任务列表_";
        String sheetName = name+nowTime;

        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        //获取列表数据
        list = taskService.getTaskListDownload(parameterMap);

        String [][] header = {{"任务ID","任务名称","地市","创建人","创建时间","开始时间","结束时间","已关联活动个数"}};
        String columns = "task_id,task_name,city_name,creator_name,create_time,start_time,end_time,relativenum";
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
