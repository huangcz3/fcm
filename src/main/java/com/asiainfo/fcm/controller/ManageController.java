package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.enums.UserRoleEnum;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.IManageService;
import com.asiainfo.fcm.task.AsyncTask;
import com.asiainfo.fcm.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/10.
 */
@RestController
@RequestMapping("/manage")
public class ManageController {

    private static final Logger logger = LoggerFactory.getLogger(ManageController.class);

    @Autowired
    private IManageService manageService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AsyncTask asyncTask;

    @Value("${fcm.sys.approver_setting.channerIds}")
    private String chnnerIds;

    /**
     * 查询所有审批人
     *
     * @param
     * @return
     */
    @GetMapping("/queryAllApprover")
    public Result queryAllApprover() {
        List<ApproverInfo> approverInfoList = manageService.queryAllApprover();
        return ResultUtil.success(approverInfoList);
    }

    /**
     * 查询客户类型（内部号码、红名单、敏感用户）
     *
     * @param phoneNo
     * @return
     */
    @GetMapping("/queryCustomerType")
    public Result queryCustomerType(@RequestParam("phoneNo") String phoneNo) {

        List<String> employeeList = new ArrayList<>();
        List<String> redList = new ArrayList<>();
        List<String> sensitiveList = new ArrayList<>();
        List<String> cancel10086List = new ArrayList<>();

        String[] phoneNos = phoneNo.split(",");

        List<Map<String, String>> customerTypeList = manageService.queryCustomerType(phoneNos);

        for (Map<String, String> map : customerTypeList) {
            String customerPhoneNo = map.get("PHONENO");
            String userType = map.get("USERTYPE");

            switch (userType) {
                case "white":
                    employeeList.add(customerPhoneNo);
                    break;
                case "red":
                    redList.add(customerPhoneNo);
                    break;
                case "black":
                    sensitiveList.add(customerPhoneNo);
                    break;
                case "10086":
                    cancel10086List.add(customerPhoneNo);
                    break;
                default:
                    break;
            }

        }

        Map<String, Object> map = new HashMap<>();

        map.put("employee", employeeList);
        map.put("redList", redList);
        map.put("sensitive", sensitiveList);
        map.put("cancel10086", cancel10086List);

        return ResultUtil.success(map);
    }

    /**
     * 新增黑白红名单
     *
     * @param blackWhiteRedUserInfo
     */
    @PostMapping("/addBlackWhiteRedUser")
    public Result addBlackWhiteRedUser(@RequestBody BlackWhiteRedUserInfo blackWhiteRedUserInfo) {
        int isExsit = manageService.isExsitByPhoneNo(blackWhiteRedUserInfo);
        if (isExsit == 0) {
            manageService.addBlackWhiteRedUser(blackWhiteRedUserInfo);
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.ISEXSIT_ERROR);
        }
    }

    /**
     * 渠道控制
     */
    @PostMapping("/channelSubClassReset")
    public Result channelSubClassReset(HttpSession session, @RequestBody List<Map> subList) throws Exception {
        try {
            User user = UserUtil.getCurrentUser(session);
            if (user.getUserRole().contains(UserRoleEnum._10086_CHANNEL.getRole())) {
                for (Map map : subList) {
                    String channelId = map.get("channelId").toString();
                    String subChannelId = map.get("subChannelId").toString();
                    String status = map.get("status").toString();
                    if ("Y".equals(status)) {  //此处“Y”表示启动，“N”表示暂停
                        if( "d05".equals(channelId) ){  //10086群发，redis规则："Y"暂停；"N"未暂停
                            stringRedisTemplate.opsForValue().set("ISPAUSED_" + channelId, "N");
                        }else{
                            stringRedisTemplate.opsForValue().set("STATUS:" + channelId + ":" + subChannelId, "Y");
                            String defaultMsg = map.get("defaultMsg").toString();
                            stringRedisTemplate.opsForValue().set("DEFAULTMSG:" + channelId + ":" + subChannelId, defaultMsg);
                        }
                    } else {
                        if( "d05".equals(channelId) ) {  //10086群发，redis规则："Y"暂停；"N"未暂停
                            stringRedisTemplate.opsForValue().set("ISPAUSED_" + channelId, "Y");
                        }else{
                            stringRedisTemplate.opsForValue().set("STATUS:" + channelId + ":" + subChannelId, "N");
                        }
                    }
                    logger.info("= = = = = subChannelId:" + subChannelId + " STATUS:" + status + " DEFAULTMSG:" + map.get("defaultMsg").toString() + "= = = = =");
                }
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取渠道控制信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/getChannelSubClassInfo")
    public Result getChannelSubClassInfo(@RequestParam("channelId") String channelId) throws Exception {
        try {
            Map resultMap = new HashMap<>();
            List<String> subClass = new ArrayList<>();
            if (channelId.equals("d02")) {
                subClass.add("1008611");
                subClass.add("1008632");
                subClass.add("CXLL");
            } else {
                subClass.add(channelId);
            }
            for (String str : subClass) {
                Map infoMap = new HashMap<>();
                String state = "";
                if ("d05".equals(str)){
                    state = stringRedisTemplate.opsForValue().get("ISPAUSED_" + channelId);
                    if ("Y".equals(state)) {
                        infoMap.put("state", "N");
                    }else{
                        infoMap.put("state","Y");
                    }
                }else{
                    state = stringRedisTemplate.opsForValue().get("STATUS:" + channelId + ":" + str);
                    infoMap.put("state", state);
                    if ("Y".equals(state)) {
                        String defaultMsg = stringRedisTemplate.opsForValue().get("DEFAULTMSG:" + channelId + ":" + str);
                        infoMap.put("defaultMsg", defaultMsg);
                    }
                }
                resultMap.put(str, infoMap);
            }
            return ResultUtil.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取用户权限信息
     *
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserRole")
    public Result getUserRole(HttpSession session) throws Exception {
        try {
            Map resultMap = new HashMap<>();
            User user = UserUtil.getCurrentUser(session);
            resultMap.put("roleInfo", user.getUserRole());
            return ResultUtil.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    @GetMapping("/reFtpImg")
    public Result reFtpImg(HttpSession session, String activityId) {
        User user = UserUtil.getCurrentUser(session);
        if (!user.getUserRole().contains(UserRoleEnum.ADMIN.getRole())) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
        Activity activity = activityService.getActivityDetailInfo(activityId);

        asyncTask.ftpImg(activity);

        return ResultUtil.success();
    }

    /**
     * 营销洞察用户id加密
     *
     * @return des加密字符串
     * @throws Exception
     */
    @GetMapping("/getDescInfo")
    public Result getDescInfo(HttpSession session) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        String str = "loginNo="+user.getUserId()+"&pwd="+user.getPassword()+"&op_time="+ DateUtil.getTimemillisecond();
        String encryptStr = PasswordUtil.encrypt(str);

        return ResultUtil.success(encryptStr);
    }
    /**
     * 推送查询
     * @param activityId 活动ID
     * @param phoneNum 电话号码
     */
   /* @GetMapping("/getPushQueryResult")
    public Result getPushQuery( @RequestParam("activityId") String activityId, @RequestParam("phoneNum") String phoneNum  ) throws Exception{
        Map parameterMap = new HashMap();
        parameterMap.put("activityId",activityId);
        parameterMap.put("phoneNum",phoneNum);

        String result = manageService.getPushQuery(parameterMap);

        return ResultUtil.success(result);
    }*/


    /**
     * 添加审批人
     *
     * @param approverInfo
     * @return
     */
    @PostMapping("/addApprover")
    public Result addApprover(HttpSession session, @RequestBody ApproverInfo approverInfo) {
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
       /* System.out.println(manageService.approverIsUnique(approverInfo));
        if (manageService.approverIsUnique(approverInfo) != 0) {
            return ResultUtil.error(ResultEnum.INFO_EXIST);
        }*/

        if (StringUtils.isNoneBlank(chnnerIds) && chnnerIds.contains(approverInfo.getChannelId())) {
            // 拼接组和角色
            if (approverInfo.getApproverLevel() <= 1)
                // 10086群发特殊处理
                if (approverInfo.getChannelId().equals("d05")) {
                    approverInfo.setApprovalRole("d051a");
                    approverInfo.setApprovalGroup("d051");
                } else {
                    approverInfo.setApprovalGroup(approverInfo.getChannelId() + 1);
                    approverInfo.setApprovalRole(approverInfo.getApprovalGroup() + approverInfo.getCityId());
                }
            else {
                approverInfo.setApprovalGroup(approverInfo.getChannelId() + approverInfo.getApproverLevel() + "at");
                approverInfo.setApprovalRole(approverInfo.getChannelId() + approverInfo.getApproverLevel() + "a");
            }
        } else {
            approverInfo.setApprovalGroup(approverInfo.getChannelId()+"_"+approverInfo.getApproverLevel());
            approverInfo.setApprovalRole(approverInfo.getApprovalGroup()+"_"+approverInfo.getCityId());
        }

        // 判断审批人是否存在
        if (manageService.approverIsUnique(approverInfo) != 0) {
            return ResultUtil.error(ResultEnum.INFO_EXIST);
        }

        manageService.addApprover(approverInfo);
        return ResultUtil.success();
    }


    /**
     * 获取审批人列表
     */
    @GetMapping("/getApproverInfoList")
    public Result getApproverInfoList(HttpServletRequest request) throws Exception {
        String cityId = request.getParameter("cityId");
        String channelId = request.getParameter("channelId");
        String searchVal = request.getParameter("searchVal");
        String approverLevel = request.getParameter("approverLevel");
        int lengthRow = Integer.parseInt(request.getParameter("lengthRow"));
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int startRow = (currentPage - 1) * lengthRow;

        //获取登录用户信息
        User user = UserUtil.getCurrentUser(request.getSession());
        String userRole = user.getUserRole();

        Map parameterMap = new HashMap<>();
        parameterMap.put("searchVal", searchVal);
        parameterMap.put("cityId", cityId);
        parameterMap.put("channelId", channelId);
        parameterMap.put("startRow", startRow);
        parameterMap.put("lengthRow", lengthRow);
        parameterMap.put("approverLevel", approverLevel);

        Map resultMap = new HashMap<>();
        List<ApproverInfo> approverList = manageService.getApproverInfoList(parameterMap);
        int totals = manageService.getApproverTotalRecords(parameterMap);

        resultMap.put("approverList", approverList);
        resultMap.put("totals", totals);
        resultMap.put("userRole", userRole);
        return ResultUtil.success(resultMap);
    }

    /**
     * 修改审批人信息
     *
     * @param approverInfo 审批人信息
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/setApprover")
    public Result setApprover(@RequestBody ApproverInfo approverInfo, HttpSession session) throws Exception {

        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }

        if (StringUtils.isNoneBlank(chnnerIds) && chnnerIds.contains(approverInfo.getChannelId())) {
            // 拼接组和角色
            if (approverInfo.getApproverLevel() <= 1)
                // 10086群发特殊处理
                if (approverInfo.getChannelId().equals("d05")) {
                    approverInfo.setApprovalRole("d051a");
                    approverInfo.setApprovalGroup("d051");
                } else {
                    approverInfo.setApprovalGroup(approverInfo.getChannelId() + 1);
                    approverInfo.setApprovalRole(approverInfo.getApprovalGroup() + approverInfo.getCityId());
                }
            else {
                approverInfo.setApprovalGroup(approverInfo.getChannelId() + approverInfo.getApproverLevel() + "at");
                approverInfo.setApprovalRole(approverInfo.getChannelId() + approverInfo.getApproverLevel() + "a");
            }
        } else {
            approverInfo.setApprovalGroup(approverInfo.getChannelId()+"_"+approverInfo.getApproverLevel());
            approverInfo.setApprovalRole(approverInfo.getApprovalGroup()+"_"+approverInfo.getCityId());
        }

        //System.out.println(manageService.approverIsUnique(approverInfo));
        if (manageService.approverIsUnique(approverInfo) != 0) {
            return ResultUtil.error(ResultEnum.INFO_EXIST);
        }

        manageService.setApprover(approverInfo);
        return ResultUtil.success();
    }

    /**
     * 删除审批人
     *
     * @param session
     * @param uuid
     * @return 0 成功
     * @throws Exception
     */
    @PostMapping("/deleteApprover")
    public Result deleteApprover(HttpSession session, @RequestParam String uuid) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        Map paramaterMap = new HashMap<>();

        paramaterMap.put("uuid", Integer.parseInt(uuid));

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
        manageService.deleteApprover(paramaterMap);
        return ResultUtil.success();
    }

    /**
     * 获取10086群发渠道控制信息
     *
     * @param channelId
     * @return
     * @throws Exception
     */
    @GetMapping("/get10086GroupSendInfo")
    public Result get10086GroupSendInfo(HttpSession session, @RequestParam("channelId") String channelId) throws Exception {

        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();
        if (userRole.contains("admin10086")) {
            Map resultMap = new HashMap<>();

            String state = stringRedisTemplate.opsForValue().get("10086_CHANNEL_STATUS:" + channelId);
            String sendFrequency = stringRedisTemplate.opsForValue().get("10086_CHANNEL_SENDING_RATE:" + channelId);
            resultMap.put("state", state);
            resultMap.put("sendFrequency", sendFrequency);

            return ResultUtil.success(resultMap);
        } else {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }

    }

    /**
     * 设置10086群发渠道控制信息
     *
     * @param state         状态
     * @param sendFrequency 发送频率
     * @return
     * @throws Exception
     */
    @PostMapping("/set10086GroupSend")
    public Result set10086GroupSend(HttpSession session, @RequestParam("channelId") String channelId, @RequestParam("state") String state,
                                    @RequestParam("sendFrequency") String sendFrequency, @RequestParam("sendCount") String sendCount) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin10086")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
        if ("Y".equals(state)) { //暂停
            stringRedisTemplate.opsForValue().set("10086_CHANNEL_STATUS:" + channelId, "Y");
            stringRedisTemplate.opsForValue().set("10086_CHANNEL_SENDING_RATE:" + channelId, "100");
        } else {
            stringRedisTemplate.opsForValue().set("10086_CHANNEL_STATUS:" + channelId, "N");
            stringRedisTemplate.opsForValue().set("10086_CHANNEL_SENDING_RATE:" + channelId, sendFrequency);
        }
        logger.info("= = = = = 10086_CHANNEL_STATUS:" + channelId + " STATUS:" + state + " 10086_CHANNEL_SENDING_RATE:" + sendFrequency + "= = = = =");
        return ResultUtil.success();
    }


    /**
     * 获取渠道短信审批信息
     *
     * @param session
     * @param searchVal   搜索变量
     * @param currentPage 当前页
     * @param lengthRow   页面大小
     * @return
     * @throws Exception
     */
    @GetMapping("/getSMSApprovalInfoList")
    public Result getSMSApprovalInfoList(HttpSession session,
                                         @RequestParam(value = "searchVal", required = false) String searchVal,
                                         @RequestParam("currentPage") long currentPage,
                                         @RequestParam("lengthRow") long lengthRow) throws Exception {
        //获取登录用户信息
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        Map parameterMap = new HashMap<>();
        parameterMap.put("searchVal", searchVal);
        parameterMap.put("startRow", (currentPage - 1) * lengthRow);
        parameterMap.put("lengthRow", lengthRow);

        Map resultMap = new HashMap<>();
        List<ChannelInfo> channelInfoList = manageService.getChannelInfoList(parameterMap);
        int totals = manageService.getChannelRecords(parameterMap);

        resultMap.put("channelInfoList", channelInfoList);
        resultMap.put("totals", totals);
        resultMap.put("userRole", userRole);
        return ResultUtil.success(resultMap);
    }

    /**
     * 修改渠道短信审批信息
     *
     * @param channelInfo 渠道信息
     * @param session
     * @return
     * @throws Exception
     */
    @PostMapping("/setSMSApproval")
    public Result setSMSApproval(@RequestBody ChannelInfo channelInfo, HttpSession session) throws Exception {

        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
        manageService.setChannelInfo(channelInfo);

        return ResultUtil.success();
    }

    /**
     * 根据场景id查询该场景下的审批层级
     *
     * @param channelId 场景id
     * @return
     */
    @GetMapping("getLevelList")
    public Result getLevelList(String channelId, HttpSession session) {
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }

        List<String> list = manageService.getLevelList(channelId);
        return ResultUtil.success(list);
    }

    /**
     * 根据场景id查询该场景下的审批的地市
     *
     * @param channelId 场景id
     * @return
     */
    @GetMapping("getCityList")
    public Result getCityList(String channelId,String level, HttpSession session) {
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        if (!userRole.contains("admin")) {
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }

        List<Map> list = manageService.getCityList(channelId,level);
        return ResultUtil.success(list);
    }
}

