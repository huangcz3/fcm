package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ActivityOpEnum;
import com.asiainfo.fcm.mapper.primary.ActivityMapper;
import com.asiainfo.fcm.mapper.primary.TaskMapper;
import com.asiainfo.fcm.mapper.tertiary.UserMapper;
import com.asiainfo.fcm.model.ActivityApproval;
import com.asiainfo.fcm.model.ActivityChannelDetail;
import com.asiainfo.fcm.model.ActivityChannelRuleDetail;
import com.asiainfo.fcm.model.ChannelApprovalDetail;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerInsightService;
import com.asiainfo.fcm.service.IMentalMapService;
import com.asiainfo.fcm.task.MentalMapTask;
import com.asiainfo.fcm.task.SmsAsyncTask;
import com.asiainfo.fcm.util.DateUtil;
import com.asiainfo.fcm.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by PuMg on 2017/6/26/0026.
 */
@Service
@Transactional
public class ActivityServiceImpl implements IActivityService {

    private Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${fcm.sms.smsNoticeCode}")
    private String smsNoticeCode;

    @Value("${fcm.sms.send.url}")
    private String smsSendUrl;

    @Autowired
    private IMentalMapService IMentalMapService;

    @Autowired
    private MentalMapTask mentalMapTask;

    @Value("${fcm.mentalMap.userFileDir}")
    private String mentalMapUserFileDir;

    @Autowired
    private SmsAsyncTask smsAsyncTask;

    @Autowired
    private ICustomerInsightService customerInsightService;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void saveActivityInfo(Activity activity) {
        activityMapper.saveActivityInfo(activity);
    }

    @Override
    @Transactional(value = "primaryTransactionManager", readOnly = false, rollbackFor = Exception.class)
    public void saveActivityAllInfo(Activity activity, User user) throws Exception {
        String activityId = activity.getActivityId();
        activity.setCreatorId(user.getUserId());
        activity.setCreatorName(user.getUserName());
        activity.setCityId(user.getCityId());
        activity.setCityName(user.getCityName());
        activity.setDeptId(user.getDeptId());
        activity.setDeptName(user.getDeptName());
        activity.setCreateTime(DateUtil.getTimeYYYYMMDDHHDDSS());

        //保存用户群
        List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList = activity.getActivityCustomerGroupInfoList();
        if (activityCustomerGroupInfoList.size() > 0) {
            List<ActivityCustomerGroupInfo> customerInsightGroup = activityCustomerGroupInfoList.stream().filter(c -> c.getCreateType() == 6).collect(Collectors.toList());

            if (customerInsightGroup.size() > 0) {
                activityCustomerGroupInfoList.removeAll(customerInsightGroup);
                List<String> batchIds = new ArrayList<>();
                List<String> names = new ArrayList<>();
                customerInsightGroup.forEach(c -> {
                    batchIds.add(c.getCustomerGroupId());
                    names.add(c.getCustomerGroupName());
                });

                ObjectMapper objectMapper = new ObjectMapper();
                ActivityCustomerGroupInfo info = customerInsightGroup.get(0);
                info.setCustomerGroupName(StringUtils.join(names, ","));
                info.setCustomerGroupId(batchIds.get(0));
                activityCustomerGroupInfoList.add(info);

                customerInsightService.getUserGroup(activityId, StringUtils.join(batchIds, ","));
            }
            activityMapper.saveCustomGroupInfo(activityCustomerGroupInfoList);
        } else {
            activity.setActivityState(1);
        }

        //保存活动基本信息
        activityMapper.saveActivityInfo(activity);

        //存入redis 默认全省的优先级高于所有地市
        String isProvince = "1".equals(user.getCityId()) ? "1" : "";
        stringRedisTemplate.opsForHash().put("ACTIVE:" + activityId, "PRIORITY", isProvince + System.currentTimeMillis());

        //设置活动优先级
        Map map = new HashMap<>();
        map.put("activityId", activityId);
        map.put("activityName", activity.getActivityName());
        map.put("cityId", user.getCityId());
        map.put("cityName", user.getCityName());
        activityMapper.saveActivityPriority(map);

        //保存渠道信息(执行渠道和审批人)
        List<ActivityChannelDetail> activityChannelDetails = activity.getActivityChannelDetailList();
        this.saveActivityChannelInfo(map, activityChannelDetails,user);

        //保存推荐业务信息
        List<ActivityRecommendProduct> activityRecommendProductList = activity.getActivityRecommendProducts();
        this.saveRecommendProductListInfo(activityRecommendProductList);


        //保存活动测试号码
        String testPhoneNum = activity.getTestPhoneNum();
        if (StringUtils.isNotBlank(testPhoneNum)) {
            String[] phoneList = testPhoneNum.split(",");
            List<String> list = Arrays.asList(phoneList);
            map.put("activityId", activityId);
            map.put("phoneList", list);
            this.saveTestPhoneNum(map);
        }

        //保存活动场景信息
        int sceneFlag = activity.getSceneFlag();
        if (sceneFlag == 1) {
            List<ActivitySceneInfo> activitySceneInfoList = activity.getActivitySceneInfoList();
            if (activitySceneInfoList.size() > 0) {
                activityMapper.saveSceneInfo(activitySceneInfoList);
            }
        }

        //保存智能地图活动信息
        if (activity.getSceneFlag() == 1 && activity.getSceneId() == 11) {
            Map mentalMap = new HashMap<>();
            mentalMap.put("taskId", activityId);
            mentalMap.put("taskName", activity.getActivityName());

            //没有上传用户群更新源目标用生成状态为 1
            if (activity.getActivityCustomerGroupInfoList().size() > 0) {
                mentalMap.put("sourceIsComplete", 0);
            } else {
                mentalMap.put("sourceIsComplete", 1);
            }

            long count1 = activity.getActivitySceneInfoList().stream().filter(s -> (s.getSceneRuleId().equals("SR013"))).count();
            if (count1 > 0) {//实时场景
                mentalMap.put("isComplete", 1);
            } else {
                mentalMap.put("isComplete", 0);
            }

            //记录智能地图客户群生成状态
            IMentalMapService.saveCustomerBuildInfo(mentalMap);

            //保存操作记录
            Map logMap = new HashMap<>();
            logMap.put("activityId", activity.getActivityId());
            logMap.put("userId", user.getUserId());
            logMap.put("userName", user.getUserName());
            logMap.put("userPhone", user.getPhoneNo());
            logMap.put("opCode", ActivityOpEnum.CREATE.getCode());
            logMap.put("opName", ActivityOpEnum.CREATE.getMsg());
            logMap.put("opTime", DateUtil.getTimeYYYYMMDDHHDDSS());
            taskMapper.recordOplog(logMap);


        }

    }

    @Override
    public void saveActivityChannelInfo(Map map, List<ActivityChannelDetail> list,User user) {

        for (ActivityChannelDetail channelDetail : list) {
            ActivityChannelInfo activityChannelInfo = new ActivityChannelInfo();
            activityChannelInfo.setActivityId(channelDetail.getActivityId());
            activityChannelInfo.setActivityName(channelDetail.getActivityName());
            activityChannelInfo.setChannelId(channelDetail.getChannelId());
            activityChannelInfo.setChannelName(channelDetail.getChannelName());
            //保存渠道信息
            List<ActivityChannelRuleDetail> ruleDetailList = channelDetail.getActivityChannelRuleDetailList();
            for (ActivityChannelRuleDetail ruleDetail : ruleDetailList) {
                activityChannelInfo.setRuleId(ruleDetail.getRuleId());
                activityChannelInfo.setRuleValue(ruleDetail.getRuleValue());
                activityChannelInfo.setRuleExtendedValue(ruleDetail.getRuleExtendedValue());
                activityMapper.saveActivityChannelInfo(activityChannelInfo);
            }
            //保存审批人信息
           /* List<ApproverInfo> approverInfoList = channelDetail.getApproverInfoList();
            if (approverInfoList != null) {
                for (ApproverInfo approverTmp : approverInfoList) {
                    //获取内容审批人
                    ApproverInfo approverInfo = activityMapper.getApproverInfo(channelDetail.getChannelId(), approverTmp.getApproverId(), approverTmp.getApproverLevel(), map.get("cityId").toString());
                    ActivityApprovalInfo activityApprovalInfo = new ActivityApprovalInfo();
                    activityApprovalInfo.setActivityId(channelDetail.getActivityId());
                    activityApprovalInfo.setActivityName(channelDetail.getActivityName());
                    activityApprovalInfo.setChannelId(channelDetail.getChannelId());
                    activityApprovalInfo.setChannelName(channelDetail.getChannelName());
                    if (approverInfo != null) {
                        activityApprovalInfo.setApproverId(approverInfo.getApproverId());
                        activityApprovalInfo.setApproverName(approverInfo.getApproverName());
                        activityApprovalInfo.setApproverPhone(approverInfo.getApproverPhone());
                        activityApprovalInfo.setApproverLevel(approverInfo.getApproverLevel());
                        activityApprovalInfo.setApprovalResult(0);
                    } else {
                        activityApprovalInfo.setApproverId("sys");
                        activityApprovalInfo.setApproverName("系统管理员");
                        activityApprovalInfo.setApproverPhone("sys");
                        activityApprovalInfo.setApproverLevel(approverTmp.getApproverLevel());
                        activityApprovalInfo.setApprovalResult(1);

                    }
                    activityMapper.saveActivityApprovalInfo(activityApprovalInfo);
                }
            } else {
                int maxLevel = activityMapper.getApprovalMaxLevel("@", map.get("cityId").toString());
                for (int i = 1; i <= maxLevel; i++) {
                    ActivityApprovalInfo activityApprovalInfo = new ActivityApprovalInfo();
                    activityApprovalInfo.setActivityId(channelDetail.getActivityId());
                    activityApprovalInfo.setActivityName(channelDetail.getActivityName());
                    activityApprovalInfo.setChannelId(channelDetail.getChannelId());
                    activityApprovalInfo.setChannelName(channelDetail.getChannelName());
                    activityApprovalInfo.setApproverId("sys");
                    activityApprovalInfo.setApproverName("系统管理员");
                    activityApprovalInfo.setApproverPhone("sys");
                    activityApprovalInfo.setApproverLevel(i);
                    activityApprovalInfo.setApprovalResult(1);
                    activityMapper.saveActivityApprovalInfo(activityApprovalInfo);
                }
            }*/


            //保存审批人 只保存审批人的组和审批角色（特定除外）
            List<Map> settings = activityMapper.getApprovalSetting(channelDetail.getChannelId(), map.get("cityId").toString());
            Map settingMap = new HashMap<>();
            settings.stream().forEach(m -> settingMap.put(m.get("APPROVAL_LEVEL"), m.get("IS_NEED_APPROVAL")));

            List<ApproverInfo> approverInfoList = channelDetail.getApproverInfoList();
            if (approverInfoList != null) {
                approverInfoList.stream().forEach(approverInfo -> {

                    ActivityApprovalInfo activityApprovalInfo = new ActivityApprovalInfo();
                    activityApprovalInfo.setActivityId(channelDetail.getActivityId());
                    activityApprovalInfo.setActivityName(channelDetail.getActivityName());
                    activityApprovalInfo.setChannelId(channelDetail.getChannelId());
                    activityApprovalInfo.setChannelName(channelDetail.getChannelName());
                    activityApprovalInfo.setApprovalGroup(approverInfo.getApprovalGroup());
                    activityApprovalInfo.setApprovalRole(approverInfo.getApprovalRole());

                    int level = approverInfo.getApproverLevel();
                    if (Integer.parseInt(settingMap.get(level).toString()) == 0) {
                        activityApprovalInfo.setApproverId("sys");
                        activityApprovalInfo.setApproverName("系统管理员");
                        activityApprovalInfo.setApproverPhone("sys");
                        activityApprovalInfo.setApproverLevel(level);
                        activityApprovalInfo.setApprovalResult(1);
                    } else {
                        String approverId = approverInfo.getApproverId();

                        activityApprovalInfo.setApproverId(approverInfo.getApproverId());
                        activityApprovalInfo.setApproverLevel(approverInfo.getApproverLevel());
                        activityApprovalInfo.setApprovalResult(0);

                        if (approverId != null && approverId != "") {
                            Map paramMap = new HashMap<>();
                            paramMap.put("channelId", channelDetail.getChannelId());
                            paramMap.put("level", level);
                            paramMap.put("approverId", approverId);
                            ApproverInfo appInfo = activityMapper.getApproverInfoByApproverId(paramMap);
                            activityApprovalInfo.setApproverName(appInfo.getApproverName());
                            activityApprovalInfo.setApproverPhone(appInfo.getApproverPhone());
                        }
                    }
                    activityMapper.saveActivityApprovalInfo(activityApprovalInfo);

                });
            }
        }

    }


    @Override
    public List<Activity> getActivityList(Map parameterMap) {
        List<Activity> activityList = activityMapper.getActivityList(parameterMap);
        String approverId = parameterMap.get("approverId").toString();
        //是否有审批权限

        for (Activity activity : activityList) {
            String activityId = activity.getActivityId();
            parameterMap.put("activityId", activityId);
            int preLevel = activityMapper.getUnApprovalMinLevel(activityId);
            if (preLevel != 0) {
                // List<String> approverIds = activityMapper.getCanApprovalIds(activityId, preLevel);
                List<String> approverIds = activityMapper.getCanApprovers(activityId, preLevel);

                if (approverIds.contains(approverId) && activity.getStopped() == 0) {
                    activity.setIsCanApproval("1");
                } else {
                    activity.setIsCanApproval("0");
                }
            } else {
                activity.setIsCanApproval("0");
            }

            //if((activity.getActivityState() !=8 || activity.getStopped() ==1) && activity.getCreatorId().equals(approverId)){
            List<String> approverInfos = activityMapper.getApproverInfos(activityId);
            if (activity.getCreatorId().equals(approverId) || approverInfos.contains(approverId)) {
                activity.setIsCanDelete(1);
            } else {
                activity.setIsCanDelete(0);
            }


            //获取目标用户生成明细
            CustomerRemoveInfo removeInfo = activityMapper.getCustomerRemoveInfo(activityId);
            activity.setCustomerRemoveInfo(removeInfo);
        }
        return activityList;
    }

    @Override
    public int getActivityListTotals(Map map) {
        return activityMapper.getActivityListTotals(map);
    }

    @Override
    public Activity getActivityDetailInfo(String activityId) {
        //获取基本信息
        Activity activityBean = activityMapper.getActivityById(activityId);
        //获取渠道信息
        List<ActivityChannelDetail> activityChannelDetailList = activityMapper.getChannelDetail(activityId);
        activityBean.setActivityChannelDetailList(activityChannelDetailList);
        //获取客户群信息
        List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList = activityMapper.getCustomGroupInfo(activityId);
        activityBean.setActivityCustomerGroupInfoList(activityCustomerGroupInfoList);
        //获取推荐业务
        List<ActivityRecommendProduct> activityRecommendProductList = activityMapper.getRecommendProduct(activityId);
        activityBean.setActivityRecommendProducts(activityRecommendProductList);
        //获取测试号码
        List<String> testPhoneList = activityMapper.getTestPhoneList(activityId);
        String testPhoneNumbers = StringUtils.join(testPhoneList, ",");
        activityBean.setTestPhoneNum(testPhoneNumbers);

        //获取场景
        if (activityBean.getSceneFlag() == 1) {
            List<ActivitySceneInfo> activitySceneInfoList = activityMapper.getSceneInfo(activityId);
            activityBean.setActivitySceneInfoList(activitySceneInfoList);
        }

        return activityBean;

    }

    @Override
    public void saveRecommendProductListInfo(List<ActivityRecommendProduct> activityRecommendProductList) {
        activityMapper.saveRecommendProductListInfo(activityRecommendProductList);
    }

    @Override
    public void saveTestPhoneNum(Map map) {
        activityMapper.saveTestPhoneNum(map);
    }

    @Override
    public void saveActivityPriority(Map map) {
        activityMapper.saveActivityPriority(map);
    }

    @Override
    public int isCanApprove(Map map) {
        return activityMapper.isCanApprove(map);
    }

    @Override
    public void saveCustomGroupInfo(List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList) {
        activityMapper.saveCustomGroupInfo(activityCustomerGroupInfoList);
    }

    @Override
    public List<ActivityCustomerGroupInfo> getCustomGroupInfo(String activityId) {
        return activityMapper.getCustomGroupInfo(activityId);
    }

    @Override
    public void approveActivityChannelInfo(ActivityApproval activityApproval, User user) {
        Map parameterMap = new HashMap<>();
        Map approvalMap = new HashMap<>();
        String activityId = activityApproval.getActivityId();
        parameterMap.put("activityId", activityId);
        parameterMap.put("approverId", user.getUserId());

        if (user.getCityId() == null) {
            Map baseMap = activityMapper.getBaseInfoByActId(activityId);
            approvalMap.put("cityId", baseMap.get("CITY_ID").toString());
        } else {
            approvalMap.put("cityId", user.getCityId());
        }

        //审批活动基本信息
        approvalMap.put("activityId", activityId);
        approvalMap.put("activityName", activityApproval.getActivityName());

        List<ChannelApprovalDetail> channelApprovalDetailList = activityApproval.getChannelApprovalDetailList();
        for (ChannelApprovalDetail channelApprovalDetail : channelApprovalDetailList) {
            parameterMap.put("channelId", channelApprovalDetail.getChannelId());
            parameterMap.put("approvalResult", channelApprovalDetail.getApprovalRsult());
            parameterMap.put("commnets", channelApprovalDetail.getApprovalComments());
            //获取当前审批人级别
            int preLevel = activityMapper.getApproverPreLevel(activityId, channelApprovalDetail.getChannelId(), user.getUserId());

            parameterMap.put("level", preLevel);
            ApproverInfo approverInfo = activityMapper.getApproverInfoByApproverId(parameterMap);
            parameterMap.put("approverName", approverInfo.getApproverName());
            parameterMap.put("approverPhone", approverInfo.getApproverPhone());
            parameterMap.put("preLevel", preLevel);
            //执行审批
            activityMapper.approveActivityChannelInfo(parameterMap);

            //审批流程处理
            approvalMap.put("channelId", channelApprovalDetail.getChannelId());
            approvalMap.put("channelName", channelApprovalDetail.getChannelName());
            boolean isPassed = channelApprovalDetail.getApprovalRsult() == 1 ? true : false;

            List<Map> settings = activityMapper.getApprovalSetting(channelApprovalDetail.getChannelId(), approvalMap.get("cityId").toString());
            Map approvalSettingMap = new HashMap<>();
            settings.stream().forEach(m -> approvalSettingMap.put(m.get("APPROVAL_LEVEL"), m.get("IS_NEED_APPROVAL")));

            boolean isChannelApproval = approvalSettingMap.get(2).equals(1);
            boolean isLeaderApproval = approvalSettingMap.get(3).equals(1);

            int isExsit = activityMapper.isExsitUnapprovalChannel(activityId, preLevel);
            if (preLevel == 1) { // 当前审批层级为内容审批时，需要等待其他渠道内容完成才进行渠道审批

                if (isExsit == 0) {//更新活动状态
                    int rejectTotals = activityMapper.getActivityRejectTimes(activityId, preLevel);
                    if (rejectTotals > 0) {
                        activityMapper.updteActivityState(activityId, 5); //内容审批有驳回 活动状态变为 活动驳回
                    } else {
                        activityMapper.updteActivityState(activityId, 4);
                    }
                } else {
                    if (!isPassed) {
                        activityMapper.updteActivityState(activityId, 5);
                    }
                }
                if (!isChannelApproval && !isLeaderApproval) {
                    saveApprovalInfoToNext(approvalMap, 3, isLeaderApproval);
                    if (isPassed) {
                        activityMapper.updteActivityState(activityId, 6);
                        //更改redis中的活动状态
                        stringRedisTemplate.opsForValue().set("CHANNELSTATUS:" + activityId + ":" + channelApprovalDetail.getChannelId(), "Y");
                        sendNoticeSms(activityId, 1, channelApprovalDetail.getChannelName());
                    } else {
                        sendNoticeSms(activityId, 2, channelApprovalDetail.getChannelName());
                    }
                } else if (isChannelApproval && isPassed) {
                    Map infoMap = new HashMap<>();
                    infoMap.put("preLevel", preLevel + 1);
                    infoMap.put("channelId", channelApprovalDetail.getChannelId());
                    infoMap.put("cityId", approvalMap.get("cityId").toString());
                    List<ApproverInfo> approverInfoList = activityMapper.getNextApproverInfos(infoMap);
                    Map smsMap = activityMapper.getSmsInfo(activityId);

                    try {
                        smsAsyncTask.sendNextSms(smsMap, approverInfoList, preLevel + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                int channelTotals = activityMapper.getChannelTotals(activityId);
                switch (preLevel) {
                    case 2:
                        int rejectTotals = activityMapper.getActivityRejectTimes(activityId, preLevel);
                        if (rejectTotals < channelTotals) {
                            if (isPassed) {
                                saveApprovalInfoToNext(approvalMap, preLevel + 1, isLeaderApproval);
                                if (!isLeaderApproval) {
                                    activityMapper.updteActivityState(activityId, 6);
                                    stringRedisTemplate.opsForValue().set("CHANNELSTATUS:" + activityId + ":" + channelApprovalDetail.getChannelId(), "Y");
                                    sendNoticeSms(activityId, 1, channelApprovalDetail.getChannelName());
                                }
                            }
                        } else {
                            activityMapper.updteActivityState(activityId, 7);
                            sendNoticeSms(activityId, 2, channelApprovalDetail.getChannelName());
                        }
                        break;
                    case 3:
                        int totals = activityMapper.getActivityRejectTimes(activityId, preLevel);
                        if (totals < channelTotals) {
                            activityMapper.updteActivityState(activityId, 6);
                            stringRedisTemplate.opsForValue().set("CHANNELSTATUS:" + activityId + ":" + channelApprovalDetail.getChannelId(), "Y");
                            sendNoticeSms(activityId, 1, channelApprovalDetail.getChannelName());
                        } else {
                            activityMapper.updteActivityState(activityId, 7);
                            sendNoticeSms(activityId, 2, channelApprovalDetail.getChannelName());
                        }
                        break;
                }
            }

        }
    }

    @Override
    public List<Map> getUnApprovalChannelInfo(Map map) {
        int preLevel = activityMapper.getUnApprovalMinLevel(map.get("activityId").toString());
        map.put("approverLevel", preLevel);
        return activityMapper.getUnApprovalChannelInfo(map);
    }

    @Override
    public int isExsitUnapprovalChannel(String activityId, int preLevel) {
        return activityMapper.isExsitUnapprovalChannel(activityId, preLevel);
    }

    /**
     * 将审批信息推送至下一级审批人
     */
    public void saveApprovalInfoToNext(Map map, int preLevel, boolean isLeaderApproval) {
        String activityId = map.get("activityId").toString();
        String channelId = map.get("channelId").toString();
        String cityId = map.get("cityId").toString();
        ActivityApprovalInfo activityApprovalInfo = new ActivityApprovalInfo();
        activityApprovalInfo.setActivityId(activityId);
        activityApprovalInfo.setActivityName(map.get("activityName").toString());
        activityApprovalInfo.setChannelId(channelId);
        activityApprovalInfo.setChannelName(map.get("channelName").toString());
/*
        Map needMap = activityMapper.getApprovalLevelSettings(channelId);
        boolean isLeaderApproval = "1".equals(needMap.get("NEEDLEADERAPPROVAL").toString()) ? true : false;*/
        if (isLeaderApproval) {
            List<ApproverInfo> approverInfoList = activityMapper.getApproverInfoById(channelId, preLevel, cityId);
//            activityApprovalInfo.setApproverId(approverInfo.getApproverId());
//            activityApprovalInfo.setApproverName(approverInfo.getApproverName());
//            activityApprovalInfo.setApproverPhone(approverInfo.getApproverPhone());
            activityApprovalInfo.setApproverLevel(approverInfoList.get(0).getApproverLevel());
            activityApprovalInfo.setApprovalGroup(approverInfoList.get(0).getApprovalGroup());
            activityApprovalInfo.setApprovalRole(approverInfoList.get(0).getApprovalRole());
            activityApprovalInfo.setApprovalResult(0);
        } else {
            activityApprovalInfo.setApproverId("sys");
            activityApprovalInfo.setApproverName("系统管理员");
            activityApprovalInfo.setApproverPhone("sys");
            activityApprovalInfo.setApproverLevel(preLevel);
            activityApprovalInfo.setApprovalResult(1);
        }
        activityMapper.saveActivityApprovalInfo(activityApprovalInfo);

        List<Map> tmp = activityMapper.getChannelSmsSetting(channelId, cityId);
        Map smsSettingMap = new HashMap<>();
        tmp.stream().forEach(m -> smsSettingMap.put(m.get("APPROVAL_LEVEL"), m.get("IS_CAN_SMS_APPROVAL")));
        //审批短信发送
        if (smsSettingMap.get(preLevel).equals(1)) {
            Map infoMap = new HashMap<>();
            infoMap.put("preLevel", preLevel);
            infoMap.put("channelId", channelId);
            infoMap.put("cityId", cityId);
            List<ApproverInfo> approverInfoList = activityMapper.getNextApproverInfos(infoMap);
            Map smsMap = activityMapper.getSmsInfo(activityId);

            try {
                smsAsyncTask.sendNextSms(smsMap, approverInfoList, preLevel);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updateActivityState(Activity activity) {
        activityMapper.updateActivityState(activity);
    }

    @Override
    public List<ActivityChannelDetail> getChannelDetail(String activityId) {
        return activityMapper.getChannelDetail(activityId);
    }

    @Override
    public void deleteActivity(String activityId) {
        activityMapper.deleteActivity(activityId);
    }

    @Override
    public Map getApprovaledResultInfo(String activityId, User user) {
        String cityId = user.getCityId();
        Map resultMap = new HashMap<>();
        int state = activityMapper.getActivityState(activityId);
        resultMap.put("step1", state);
        if (state != 0 || state != 2 || state != 3) {
//            int isExsit = activityMapper.isExsitUnapprovalChannel(activityId, 1);
//            if (isExsit == 0) {
//                //int Level = activityMapper.getApprovaledMaxLevel(activityId);
//                int Level = activityMapper.getUnApprovalMinLevel(activityId);
//                for (int i = 1; i <= Level; i++) {
//                    List<ActivityApprovalInfo> activityApprovalInfoList = activityMapper.getActivityApprovalResult(activityId, i);
//                    if (activityApprovalInfoList.size() > 0) {
//                        resultMap.put("step" + (i + 1), activityApprovalInfoList);
//                    }
//                }
//            } else {
//                List<ActivityApprovalInfo> activityApprovalInfoList = activityMapper.getActivityApprovalResult(activityId, 1);
//                resultMap.put("step2", activityApprovalInfoList);
//            }
            //int maxLevel = activityMapper.getchannelMaxLevel(channelId,cityId);
//            List<String> channels = activityMapper.getChannels(activityId);
//            for(String channelId : channels){
//
//            }
            List<String> channels = activityMapper.getChannels(activityId);
            for (String channelId : channels) {
                int maxLevel = activityMapper.getchannelMaxLevel(channelId, cityId);
                for (int i = 1; i <= maxLevel; i++) {
                    List<ActivityApprovalInfo> activityApprovalInfoList = activityMapper.getActivityApprovalResult(activityId, i);
                    if (activityApprovalInfoList.size() > 0) {
                        resultMap.put("step" + (i + 1), activityApprovalInfoList);
                    } else { //查询审批人信息

                    }

                }
            }
        }
        return resultMap;
    }

    @Override
    public int isExsitByActivityId(String activityId) {
        return activityMapper.isExsitByActivityId(activityId);
    }

    //活动审批通过 发送通知短信
    public void sendNoticeSms(String activityId, int approvalResult, String channelName) {
        Activity activity = activityMapper.getActivityById(activityId);
        Map sendMap = new HashMap<String, String>();
        sendMap.put("touchCyc", "0");
        sendMap.put("channelType", "ZDXC");
        sendMap.put("srcId", smsNoticeCode);
        sendMap.put("srcId", smsNoticeCode);
        //sendMap.put("tel", "13880584836");

        User user = userMapper.queryUser(activity.getCreatorId());
        sendMap.put("tel", user.getPhoneNo());

        int type = approvalResult == 1 ? 2 : 3;
        String msg = activityMapper.smsTemplateContent(type);
        String temp = activity.getActivityName() + "(" + activity.getActivityId() + ")" + "执行渠道:" + channelName;
        msg = msg.replaceAll("#_1", temp);
        sendMap.put("msg", msg);
        HttpUtil httpUtil = new HttpUtil();
        HttpUtil.sendGet(smsSendUrl, sendMap);
        logger.info("= = = = = 活动审批结束短信已发送,发送号码：" + activity.getCreatorId() + " = = = = =");
    }

    @Override
    public void pausedAcitivity(String activityId) {
        activityMapper.pausedAcitivity(activityId);
    }

    @Override
    public void recoveryActivity(String activityId) {
        activityMapper.recoveryActivity(activityId);
    }

    @Override
    public String getCreatorId(String activityId) {
        return activityMapper.getCreatorId(activityId);
    }

    @Override
    public void saveApprovalByBackground(Map map) {
        int isApprovaled = activityMapper.isApprovaled(map);
        if (isApprovaled == 0) {
            ActivityApproval activityApproval = new ActivityApproval();
            activityApproval.setActivityId(map.get("activityId").toString());
            activityApproval.setActivityName("iop"); //TODO 获取active name 暂时使用iop

            ChannelApprovalDetail channelApprovalDetail = new ChannelApprovalDetail();
            channelApprovalDetail.setChannelId(map.get("channelId").toString());
            channelApprovalDetail.setChannelName("互联网");
            int approvalResult = Integer.parseInt(map.get("approvalResult").toString());
            channelApprovalDetail.setApprovalRsult(approvalResult);
            if (approvalResult == 2) {
                String approvalComments = map.getOrDefault("approvalComments","后台审批驳回").toString();
                channelApprovalDetail.setApprovalComments(approvalComments);
            }
            List<ChannelApprovalDetail> channelApprovalDetailList = new ArrayList<>();
            channelApprovalDetailList.add(channelApprovalDetail);
            activityApproval.setChannelApprovalDetailList(channelApprovalDetailList);

            User user = new User();
            user.setUserId(map.get("approverId").toString());


            //执行审批
            approveActivityChannelInfo(activityApproval, user);

            logger.info("= = = = = 后台审执行成功,活动id:" + map.get("activityId").toString() + "层级：" + map.get("preLevel"));
        } else {
            logger.info("= = = = = 后台审执行失败,原因重复审批");
        }
    }

    @Override
    public int isCanOp(String userId, String activityId) {
        return activityMapper.isCanOp(userId, activityId);
    }

    @Override
    public List<Map<String, String>> getAllActivityList(Map parameterMap) {
        List<Map<String, String>> allList = activityMapper.getAllActivityList(parameterMap);
        for (Map rowMap : allList) {
            String state = rowMap.get("ACTIVITY_STATE").toString();
            if (state.equals("0")) {
                rowMap.put("ACTIVITY_STATE", "系统判定中");
            } else if (state.equals("1")) {
                rowMap.put("ACTIVITY_STATE", "待审批");
            } else if (state.equals("2") || state.equals("3")) {
                rowMap.put("ACTIVITY_STATE", "系统判定失败");
            } else if (state.equals("4")) {
                rowMap.put("ACTIVITY_STATE", "审批中");
            } else if (state.equals("5") || state.equals("7")) {
                rowMap.put("ACTIVITY_STATE", "审批驳回");
            } else if (state.equals("8")) {
                rowMap.put("ACTIVITY_STATE", "执行中");
            } else {
                rowMap.put("ACTIVITY_STATE", "执行结束");
            }
        }
        return allList;
    }

    @Override
    public List<Map> getActivityOpLogs(String activityId) {
        return activityMapper.getActivityOpLogs(activityId);
    }

    @Override
    public List<String> getActivities2Toutiao() {
        return activityMapper.getActivities2Toutiao();
    }

    @Override
    public List<String> getActivities2Wechat() {
        return activityMapper.getActivities2Wechat();
    }


}
