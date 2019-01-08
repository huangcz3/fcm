package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ActivityOpEnum;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.model.ActivityApproval;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.IBaseInitService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.service.ITaskService;
import com.asiainfo.fcm.task.AsyncTask;
import com.asiainfo.fcm.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动管理.
 * Created by RUOK on 2017/6/16.
 */
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IBaseInitService baseService;

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AsyncTask asyncTask;

    @Value("${fcm.customer-group.max-records}")
    private long customerGroupMaxRecords;

    @Autowired
    private ITaskService taskService;

    /**
     * @param request
     * @return
     * @throws Exception
     * @Author MgPu
     */
    @RequestMapping("/getActBaseInit")
    @ResponseBody

    public Result getActBaseInit(HttpServletRequest request) throws Exception {
        Map resutlMap = new HashMap<>();
        String activityId = IDUtil.generateActivityId();
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            //获取地址名称
            String cityId = user.getCityId();
            String cityName = user.getCityName();
            Map actBaseInfo = new HashMap<>();
            actBaseInfo.put("activityId", activityId);
            actBaseInfo.put("cityId", cityId);
            actBaseInfo.put("cityName", cityName);

            Map map = new HashMap<>();
            map.put("cityId", cityId);
            map.put("deptId", user.getDeptId());

            //获取场景下拉
            List<SceneInfo> sceneList = baseService.getSceneInit(cityId);
            //获取渠道下拉
            List<ChannelInfo> channelInfoList = baseService.getChanelInitByCityId(cityId);
            //获取业务类型
            List<BusinessType> businessTypeList = baseService.getBusinessList();
            //获取营销目的
            List<MarketingPurpose> marketingPurposeList = baseService.getMarketingPurposes();
            //获取客户群选取入口
            List<Map> customerList = baseService.getCustomerList(map);


            resutlMap.put("actBaseInfo", actBaseInfo);
            resutlMap.put("sceneList", sceneList);
            resutlMap.put("channelInfoList", channelInfoList);
            resutlMap.put("businessTypeList", businessTypeList);
            resutlMap.put("marketingPurposeList", marketingPurposeList);
            resutlMap.put("customerList", customerList);
            return ResultUtil.success(resutlMap);
        } else {
            return ResultUtil.error(ResultEnum.NOT_LOGIN);
        }
    }

    /**
     * 保存活动信息
     *
     * @param activity
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/saveActivityInfo")
    public Result saveActivityInfo(@RequestBody Activity activity, HttpSession session) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        int isExsit = activityService.isExsitByActivityId(activity.getActivityId());
        if (isExsit == 0) {

            //非单掌厅渠道时判断目标用户数是否超额
/*            if (!(activity.getActivityChannelDetailList().size() == 1 && "q03".equals(activity.getActivityChannelDetailList().get(0).getChannelId()))) {

                long amount = 0;
                CustomerGroupInfo customerGroupInfo;

                List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList = activity.getActivityCustomerGroupInfoList();
                for (ActivityCustomerGroupInfo activityCustomerGroupInfo : activityCustomerGroupInfoList) {

                    int createType = activityCustomerGroupInfo.getCreateType();

                    switch (createType) {//1标签库，2数据集市，3文件上传，4数字内容
                        case 1:
                            Map<String, Object> map = Collections.singletonMap("cocCustomerGroupId", activityCustomerGroupInfo.getCustomerGroupId());
                            customerGroupInfo = customerGroupService.getCocCustomerGroup(map);
                            amount += customerGroupInfo.getAmount();
                            break;
                        case 2:
                            break;
                        case 3:
                            customerGroupInfo = customerGroupService.getCustomerGroup(activityCustomerGroupInfo);
                            amount += customerGroupInfo.getAmount();
                            break;
                        case 4:
                            break;
                        default:
                            break;
                    }
                }
                if (amount > customerGroupMaxRecords) {
                    return ResultUtil.error(ResultEnum.MAX_RECORDS_EXCEEDED);
                }
            }*/

            activityService.saveActivityAllInfo(activity, user);

            if (activity.getActivityCustomerGroupInfoList().size() > 0) {
                boolean flag = asyncTask.processCustomerGroup(session, activity, user);

                if (flag) {
                    //计算客户群
                    asyncTask.calculateCustomerGroup(activity, user);
                }
            }

            //活动基本信息压入redis
            asyncTask.toRedisOfJson(activity);

            //上传图片
            asyncTask.ftpImg(activity);

            return ResultUtil.success(ResultEnum.SUCCESS);
        } else {
            return ResultUtil.success(ResultEnum.ISEXSIT_ACT_ERROR);
        }
    }

    /**
     * 查询活动列表
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
        String showState = request.getParameter("showState");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        int lengthRow = Integer.parseInt(request.getParameter("lengthRow"));
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int startRow = (currentPage - 1) * lengthRow;

        //获取登录用户信息
        User user = UserUtil.getCurrentUser(request.getSession());
        String userId = user.getUserId();
        String currentCity = user.getCityId();
        String deptId = user.getDeptId();

        Map parameterMap = new HashMap<>();
        parameterMap.put("searchVal", searchVal);
        parameterMap.put("cityId", cityId);
        parameterMap.put("currentCity", currentCity);
        parameterMap.put("channelId", channelId);
        parameterMap.put("showState", showState);
        parameterMap.put("startRow", startRow);
        parameterMap.put("lengthRow", lengthRow);
        parameterMap.put("startTime", startTime);
        parameterMap.put("endTime", endTime);
        parameterMap.put("approverId", userId);
        parameterMap.put("deptId", deptId);

        List<Activity> activityList = activityService.getActivityList(parameterMap);
        int allTotals = activityService.getActivityListTotals(parameterMap);

        resultMap.put("activityList", activityList);
        resultMap.put("allTotals", allTotals);

        return ResultUtil.success(resultMap);
    }

    /**
     * 获取详情
     *
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/getActivityDetail")
    public Result getActivityDetail(@RequestParam String activityId) throws Exception {
        Activity activity = activityService.getActivityDetailInfo(activityId);
        return ResultUtil.success(activity);
    }

    /**
     * 活动审批
     *
     * @param session
     * @param activityApproval
     * @return
     * @throws Exception
     */
    @PostMapping("/saveApproveActivity")
    public Result ApproveActivity(HttpSession session, @RequestBody ActivityApproval activityApproval) throws Exception {
        Map resultMap = new HashMap<>();
        User user = UserUtil.getCurrentUser(session);
        activityService.approveActivityChannelInfo(activityApproval, user);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取可审批渠道信息
     *
     * @param session
     * @param activityId
     * @return
     */
    @GetMapping("/getCanApprovalInfo")
    public Result getCanApprovalInfo(HttpSession session, @RequestParam String activityId) {
        Map resultMap = new HashMap<>();
        User user = UserUtil.getCurrentUser(session);
        Map parameterMap = new HashMap<>();
        parameterMap.put("activityId", activityId);
        parameterMap.put("approverId", user.getUserId());
        List<Map> mapList = activityService.getUnApprovalChannelInfo(parameterMap);
        resultMap.put("channelList", mapList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 删除活动
     *
     * @param session
     * @param activityId
     * @return 0 成功
     * @throws Exception
     */
    @PostMapping("/deleteActivity")
    public Result deleteActivity(HttpSession session, @RequestParam String activityId) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        int isCanOp = activityService.isCanOp(user.getUserId(), activityId);
        if (isCanOp > 0) {
            activityService.deleteActivity(activityId);
            stringRedisTemplate.opsForValue().set("ISPAUSED:" + activityId, "Y");

            recordOperationLog(activityId, user, "DELETE");

            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    /**
     * 获取审批结果（按阶段展示）
     *
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/getApprovaledResultInfo")
    public Result getApprovaledResultInfo(HttpSession session, @RequestParam String activityId) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        Map resultMap = activityService.getApprovaledResultInfo(activityId, user);
        return ResultUtil.success(resultMap);
    }

    /**
     * 活动暂停
     *
     * @param session
     * @param activityId
     * @return 0 成功
     * @throws Exception
     */
    @PostMapping("/pausedActivity")
    public Result pausedActivity(HttpSession session, @RequestParam String activityId) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        int isCanOp = activityService.isCanOp(user.getUserId(), activityId);
        if (isCanOp > 0) {
            activityService.pausedAcitivity(activityId);
            stringRedisTemplate.opsForValue().set("ISPAUSED:" + activityId, "Y");

            recordOperationLog(activityId, user, "PAUSE");

            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    /**
     * 恢复活动
     *
     * @param session
     * @param activityId
     * @return 0 成功
     * @throws Exception
     */
    @PostMapping("/recoveryActivity")
    public Result recoveryActivity(HttpSession session, @RequestParam String activityId) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        int isCanOp = activityService.isCanOp(user.getUserId(), activityId);
        if (isCanOp > 0) {
            activityService.recoveryActivity(activityId);
            stringRedisTemplate.opsForValue().set("ISPAUSED:" + activityId, "N");

            recordOperationLog(activityId, user, "RECOVERY");

            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    /**
     * 活动审批后台调用接口
     *
     * @param activityId     活动id
     * @param channelId      渠道id
     * @param approverId     审批id
     * @param preLevel       审批层级
     * @param approvalResult 审批结果 1通过 2驳回
     * @return 0 成功
     * @throws Exception
     */
    @GetMapping("/saveApprovalByBackground")
    public Result saveApprovalByBackground(@RequestParam String activityId, @RequestParam String channelId, @RequestParam String approverId, @RequestParam int preLevel, @RequestParam int approvalResult) throws Exception {
        Map parameterMap = new HashMap<>();
        parameterMap.put("activityId", activityId);
        parameterMap.put("channelId", channelId);
        parameterMap.put("approverId", approverId);
        parameterMap.put("preLevel", preLevel);
        parameterMap.put("approvalResult", approvalResult);

        activityService.saveApprovalByBackground(parameterMap);
        return ResultUtil.success();
    }

    @GetMapping("/getActivityListDownload")
    public Result getActivityListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);
        String currentCity = user.getCityId();
        String deptId = user.getDeptId();

        Map resultMap = new HashMap<>();
        //获取参数
        String searchVal = request.getParameter("searchVal");
        String cityId = request.getParameter("cityId");
        String channelId = request.getParameter("channelId");
        String showState = request.getParameter("showState");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        Map parameterMap = new HashMap<>();

        parameterMap.put("searchVal", searchVal);
        parameterMap.put("cityId", cityId);
        parameterMap.put("currentCity", currentCity);
        parameterMap.put("channelId", channelId);
        parameterMap.put("showState", showState);
        parameterMap.put("startTime", startTime);
        parameterMap.put("endTime", endTime);
        parameterMap.put("deptId", deptId);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());
        String name = "营销活动_";
        String sheetName = name + nowTime;

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        //获取列表数据
        list = activityService.getAllActivityList(parameterMap);

        String[][] header = {{"活动ID", "活动名称", "地市", "活动状态", "创建人", "创建时间", "开始时间", "结束时间", "目标客户数", "执行渠道"}};
        String columns = "activity_id,activity_name,city_name,activity_state,creator_name,create_time,start_time,end_time,final_amount,channel_name";
        exportComplexExcel(request, response, sheetName, header, columns, list);

        return ResultUtil.success();
    }

    public static <T> void exportComplexExcel(HttpServletRequest request, HttpServletResponse response, String sheetName, String[][] header, String columns, List<T> data) throws Exception {
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

    void recordOperationLog(String activityId, User user, String tag) {
        //日志记录
        Map logMap = new HashMap<>();
        logMap.put("activityId", activityId);
        logMap.put("userId", user.getUserId());
        logMap.put("userName", user.getUserName());
        logMap.put("userPhone", user.getPhoneNo());
        logMap.put("opCode", ActivityOpEnum.valueOf(tag).getCode());
        logMap.put("opName", ActivityOpEnum.valueOf(tag).getMsg());
        logMap.put("opTime", DateUtil.getTimeYYYYMMDDHHDDSS());

        taskService.recordOpLog(logMap);
    }

    /**
     * 获取活动操作记录
     *
     * @param session
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/getOpLogs")
    public Result getActivityOplogs(HttpSession session, @RequestParam("activityId") String activityId) throws Exception {
        List<Map> logInfo = activityService.getActivityOpLogs(activityId);
        return ResultUtil.success(logInfo);
    }


    /**
     * 保存活动详情到redis详情
     *
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/saveActivityDetailToRedis")
    public Result saveActivityDetailToRedis(@RequestParam String activityId) throws Exception {
        Activity activity = activityService.getActivityDetailInfo(activityId);
        asyncTask.toRedisOfJson(activity);
        return ResultUtil.success();
    }

}