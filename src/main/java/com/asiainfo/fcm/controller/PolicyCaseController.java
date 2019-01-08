package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.PolicyCaseActivity;
import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IPolicyCaseService;
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
 * Created by ShaoJinyu on 2017/12/12.
 */
@RestController
@RequestMapping("/policyCase")
public class PolicyCaseController {
    @Autowired
    private IPolicyCaseService policyCaseService;

    @GetMapping("/getNewActBasicInfo")
    public Result getNewActBasicInfo() throws Exception{
        Map result = new HashMap();
        String caseId = IDUtil.generatePolicySceneActId();
        result.put("activityId",caseId);
        return ResultUtil.success(result);
    }

    @PostMapping("/saveNewPolicyCase")
    public Result saveNewPolicyCase(@RequestBody PolicyCaseActivity caseActivity, HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();
        caseActivity.setCreateId(userId);
        caseActivity.setCreateName(userName);
        caseActivity.setProId("280");

        policyCaseService.saveNewPolicyCase(caseActivity);

        return ResultUtil.success();
    }

    @GetMapping("/getPolicyCaseList")
    public Result getPolicyCaseList(HttpSession session,@RequestParam("caseType") String caseType,@RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime,@RequestParam("searchVal") String searchVal,@RequestParam("pageSize") String pageSize,@RequestParam("page") String page) throws Exception{
        Map parameterMap = new HashMap();
        parameterMap.put("caseType",caseType);
        parameterMap.put("startTime",startTime);
        parameterMap.put("endTime",endTime);
        parameterMap.put("searchVal",searchVal);
        int lengthRow = Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(page);
        int startRow = (currentPage - 1)*lengthRow;
        parameterMap.put("lengthRow",lengthRow);
        parameterMap.put("startRow",startRow);

        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        parameterMap.put("userId",userId);

        Map result = new HashMap();
        List<PolicyCaseActivity> caseActivityList = policyCaseService.getPolicyCaseList(parameterMap);
        int totals = policyCaseService.getPolicyCaseListTotals(parameterMap);
        result.put("activityList",caseActivityList);
        result.put("totals",totals);
        return ResultUtil.success(result);
    }

    @PostMapping("/uploadPolicyCase")
    public Result uploadPolicyCase(@RequestParam("activityId") String activityId,HttpSession session) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("activityId",activityId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policyCaseService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policyCaseService.uploadActivity(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @PostMapping("deletePolicyCase")
    public Result deletePolicyCase(@RequestParam("activityId") String activityId,HttpSession session) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("activityId",activityId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policyCaseService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policyCaseService.deletePolicyCase(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @GetMapping("getPolicyCaseDetail")
    public Result getPolicyCaseDetail(@RequestParam("activityId") String caseId) throws Exception{
        PolicyCaseActivity caseActivity = policyCaseService.getPolicyCaseDetail(caseId);
        return ResultUtil.success(caseActivity);
    }

    @GetMapping("/getPolicyCaseListDownload")
    public Result getPolicyCaseListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);

        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String caseType = request.getParameter("caseType");

        Map parameterMap = new HashMap<>();

        parameterMap.put("searchVal", searchVal);
        parameterMap.put("startTime",startTime);
        parameterMap.put("endTime", endTime);
        parameterMap.put("caseType", caseType);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String name = caseType.equals("1")? "四川_":"集团_";
        String sheetName = name+now;

        List<Map<String,String>> list = new ArrayList<Map<String, String>>();
        //获取列表数据
        list = policyCaseService.getAllPolicyCaseList(parameterMap);

        String [][] header = {{"案例名称","上报人","省份","上报时间","目的","思路","亮点","效果","结论","附件名称","状态"}};
        String columns = "title,uploadName,proName,upDateTime,caseObjective,mind,keyPoint,kpi,result,attachmentName,flag";
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
