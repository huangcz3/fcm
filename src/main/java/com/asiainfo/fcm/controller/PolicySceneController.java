package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.PolicySceneActivity;
import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IActivityEvalutionService;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.IPolicySceneService;
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
 * Created by ShaoJinyu on 2017/12/6.
 */
@RestController
@RequestMapping("/policyScene")
public class PolicySceneController {
    @Autowired
    private IPolicySceneService policySceneService;

    @Autowired
    private IActivityService activityService;

    @GetMapping("/getNewActBasicInfo")
    public Result getNewActBasicInfo() throws Exception{
        Map resultMap = new HashMap();
        String activityId = IDUtil.generatePolicySceneActId();
        String sgmtId = IDUtil.generatePolicySgmtId();
        String offerCode = IDUtil.generatePolicyOfferCode();

        resultMap.put("activityId",activityId);
        resultMap.put("sgmtId",sgmtId);
        resultMap.put("offerCode",offerCode);

        return ResultUtil.success(resultMap);
    }

    @PostMapping("/savePolicySceneActivity")
    public Result savePolicySceneActivity(@RequestBody PolicySceneActivity policySceneActivity, HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String createId = user.getUserId();
        String createName = user.getUserName();
        policySceneActivity.setProId("280");
        policySceneActivity.setCreateId(createId);
        policySceneActivity.setCreateName(createName);
        policySceneService.savePolicySceneActivity(policySceneActivity);
        return ResultUtil.success();
    }

    @GetMapping("/getPolicySceneList")
    public Result getPolicySceneList(HttpSession session,@RequestParam("searchVal") String searchVal,@RequestParam("activityType") String activityType, @RequestParam("activityStartTime") String activityStartTime, @RequestParam("activityEndTime") String activityEndTime, @RequestParam("sceneType") String sceneType, @RequestParam("lengthRow") String lengthRow, @RequestParam("page") String page) throws Exception{
        Map paramMap = new HashMap();
        paramMap.put("searchVal",searchVal);
        paramMap.put("activityType",activityType);
        paramMap.put("startTime",activityStartTime);
        paramMap.put("endTime",activityEndTime);
        paramMap.put("sceneType",sceneType);
        int pageSize = Integer.parseInt(lengthRow);
        int currentPage = Integer.parseInt(page);
        int startRow = (currentPage - 1)*pageSize;
        paramMap.put("lengthRow",pageSize);
        paramMap.put("startRow",startRow);

        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        paramMap.put("createId",userId);

        Map resultMap = new HashMap();
        List<PolicySceneActivity> activityList =  policySceneService.getPolicySceneList(paramMap);
        int totals = policySceneService.getPolicySceneListTotals(paramMap);

        resultMap.put("activityList",activityList);
        resultMap.put("totals",totals);
        return ResultUtil.success(resultMap);
    }

    @PostMapping("/uploadSceneActivity")
    public Result uploadActivity(@RequestParam("activityId") String activityId, HttpSession session ) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("activityId",activityId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policySceneService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policySceneService.uploadActivity(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @PostMapping("/deleteSceneActivity")
    public Result deleteSceneActivity(@RequestParam("activityId") String activityId,HttpSession session) throws Exception{
        Map paramMap = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userName = user.getUserName();

        paramMap.put("activityId",activityId);
        paramMap.put("userId",userId);
        paramMap.put("uploadName",userName);

        int isCanUp = policySceneService.isCanUp(paramMap);
        if (isCanUp > 0) {
            policySceneService.deleteSceneActivity(paramMap);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    @GetMapping("/getSceneActivityDetail")
    public Result getSceneActivityDetail(@RequestParam("activityId") String activityId){
        PolicySceneActivity activityDetails = policySceneService.getSceneActivityDetail(activityId);
        return ResultUtil.success(activityDetails);
    }

    /**
     * 查询关联活动列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getActivityList")
    public Result getActivityList(HttpServletRequest request) throws Exception {
        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String cityId = request.getParameter("cityId");
        String channelId = request.getParameter("channelId");
//        String showState = request.getParameter("showState");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        int lengthRow = Integer.parseInt(request.getParameter("lengthRow"));
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int startRow = (currentPage - 1) * lengthRow;

        //获取登录用户信息
        User user = UserUtil.getCurrentUser(request.getSession());
        String userId = user.getUserId();
        String currentCity = user.getCityId();

        Map parameterMap = new HashMap<>();
        parameterMap.put("searchVal", searchVal);
        parameterMap.put("cityId", cityId);
        parameterMap.put("currentCity",currentCity);
        parameterMap.put("channelId", channelId);
        parameterMap.put("startRow", startRow);
        parameterMap.put("lengthRow", lengthRow);
        parameterMap.put("startTime", startTime);
        parameterMap.put("endTime", endTime);
        parameterMap.put("approverId", userId);
        parameterMap.put("showState", "7");

        Map parameterMap2 = new HashMap();
        parameterMap2.putAll(parameterMap);
        parameterMap2.put("showState","8");


//        List<Activity> activityList = activityService.getActivityList(parameterMap);
//        int allTotals = activityService.getActivityListTotals(parameterMap);

        List<Activity> activityList2 = activityService.getActivityList(parameterMap2);
        int allTotals2 = activityService.getActivityListTotals(parameterMap2);

//        activityList.addAll(activityList2);

        resultMap.put("activityList", activityList2);
        resultMap.put("allTotals",allTotals2);

        return ResultUtil.success(resultMap);
    }

    @GetMapping("/getRelativeActivity")
    public Result getRelativeActivity(@RequestParam("activityId") String activityId) throws Exception{
        Activity activity = activityService.getActivityDetailInfo(activityId);
        Map<String,Object> indexs = policySceneService.getActivityEvalutionIndexs(activityId);
        Map result = new HashMap();
        result.put("activityInfo",activity);
        result.put("indexs",indexs);
        return ResultUtil.success(result);
    }

    @GetMapping("/getPolicySceneListDownload")
    public Result getPolicySceneListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);

        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String activityType = request.getParameter("activityType");
        String activityStartTime = request.getParameter("activityStartTime");
        String activityEndTime = request.getParameter("activityEndTime");
        String sceneType = request.getParameter("sceneType");

        Map parameterMap = new HashMap<>();

        parameterMap.put("searchVal", searchVal);
        parameterMap.put("activityType", activityType);
        parameterMap.put("activityStartTime",activityStartTime);
        parameterMap.put("activityEndTime", activityEndTime);
        parameterMap.put("sceneType", sceneType);

       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = df.format(new Date());
        String name = sceneType.equals("1")? "四川_":"集团_";
        String sheetName = name+now;

        //获取列表数据
        List<Map<String,String>> list = policySceneService.getAllPolicySceneList(parameterMap);

        String [][] header = {{"营销活动名称","上报人","创建人","省份","上报日期","创建日期","PCC策略","活动开始时间","活动结束时间","营销活动目的","营销活动类型","营销活动描述","状态","目标客户群","目标客户群描述",
        "目标客户群筛选规则","产品名称","产品分类","产品描述","渠道名称","营销用语","时机","时间规则","时机描述","渠道接触规则","成功接触客户数"," 接触成功率",
        "营销成功用户数","营销成功率","响应率","投入产出比"}};
        String columns = "activityName,uploadName,createName,proName,uploadTime,createTime,isPCC,activityStartTime,activityEndTime,activityObjective,activityType,activityDescribe,flag,sgmtName,sgmtDesc," +
                "sgmtSiftRule,offerName,offerType,offerDesc,channelName,marketInfo,timeId,timeRule,timeDistindes,channelRule,succContactUserNum,succContactRatio," +
                "succMarketUserNum,succMarketRatio,responseRatio,inputOutputRatio";
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
