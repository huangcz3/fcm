package com.asiainfo.fcm.task;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivityApprovalInfo;
import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.mapper.primary.SMSTaskMapper;
import com.asiainfo.fcm.model.*;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ISMSTaskService;
import com.asiainfo.fcm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短息审批发送处理
 * Created by PuMg on 2017/12/7/0007.
 */
@Component
public class SmsAsyncTask {
    private Logger logger = LoggerFactory.getLogger(SmsAsyncTask.class);

    @Autowired
    private ISMSTaskService smsTaskService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private SMSTaskMapper smsTaskMapper;

    @Value("${fcm.sms.send.interval}")
    private int sendInterval;

    @Value("${fcm.sms.reply.validate}")
    private int validate;

    //短信发送号码
    private static final String SEND_CODE = "10086042";

    //发送短信接口
    @Value("${fcm.sms.send.url}")
    private String SEND_URL;

    //短信回复接口
    @Value("${fcm.sms.reply.url}")
    private String REPLY_URL;

    /**
     * 审批短信发送接口
     * @param activity
     */
    @Async
    public void sendApprovalSms(Activity activity) throws InterruptedException {
        ActivityApprovalSmsInfo approvalSmsInfo = new ActivityApprovalSmsInfo();
        Map smsInfoMap = new HashMap<>();
        approvalSmsInfo.setActivityId(activity.getActivityId());
        approvalSmsInfo.setActivityName(activity.getActivityName());
        List<ActivityChannelDetail> channelDetailList = activity.getActivityChannelDetailList();
        for(ActivityChannelDetail channelDetail : channelDetailList){
            approvalSmsInfo.setChannelId(channelDetail.getChannelId());
            approvalSmsInfo.setChannelName(channelDetail.getChannelName());
            List<ActivityChannelRuleDetail> ruleDetailList = channelDetail.getActivityChannelRuleDetailList().stream().filter(rule -> "R001".equals(rule.getRuleId())).collect(Collectors.toList());
            String activityContent =  ruleDetailList.get(0).getRuleValue();
            approvalSmsInfo.setActivityContent(activityContent);

            channelDetail.getApproverInfoList().stream().filter(approverInfo1 -> "2".equals(approverInfo1.getApproverLevel()));
            ApproverInfo approverInfo = channelDetail.getApproverInfoList().get(0);
            approvalSmsInfo.setApproverLevel(approverInfo.getApproverLevel());

            //是否支持短信审批
            Map tmpMap = new HashMap<>();
            tmpMap.put("channelId",channelDetail.getChannelId());
            tmpMap.put("cityId",activity.getCityId());
            tmpMap.put("approvalLevel",approverInfo.getApproverLevel());
            int isCanSmsApproval = smsTaskService.isCanSmsApproval(tmpMap);
            if(isCanSmsApproval == 1){
                String smsContent = buildSmsContent(activity,approvalSmsInfo,approverInfo.getApproverLevel());
                approvalSmsInfo.setActivityContent(smsContent);

                List<ApproverInfo> approverInfos = smsTaskMapper.getApproverInfo(approverInfo);
                sendSms(approvalSmsInfo,approverInfos);
            }
            //logger.info("approverInfoList {}",approverInfoList.toString());
        }
    }

    /**
     * 构建短信发送内容
     * @return
     */
    public String buildSmsContent(Activity activity, ActivityApprovalSmsInfo approvalSmsInfo, int level){
        String template = smsTaskService.getSmsTemplate(level);
        String  smsContent = "由"+activity.getCreatorName()+"创建"
                +",活动名称:"+activity.getActivityName()+"("+activity.getActivityId()+")"
                +",归属地市:"+activity.getCityName()
                +",执行渠道:"+approvalSmsInfo.getChannelName()
                +",目标用户数"+activity.getFinalAmount()
                +",活动内容:"+approvalSmsInfo.getActivityContent()
                +",开始时间:"+activity.getStartTime()
                +",结束时间:"+activity.getEndTime();
        template = template.replaceAll("#_1",smsContent);
        if(level>1){
            Map parameterMap = new HashMap<>();
            parameterMap.put("activityId",activity.getActivityId());
            parameterMap.put("channelId",approvalSmsInfo.getChannelId());
            parameterMap.put("level",level-1);

            ApproverInfo approverInfo = smsTaskService.getPreApproverInfo(parameterMap);
            String temp = approverInfo.getApproverName()+"("+approverInfo.getApproverPhone()+")";
            template = template.replaceAll("#_1",smsContent).replaceAll("#_2",temp);
        }
        return template;
    }

    /**
     * 审批短息发送
     * @param approvalSmsInfo
     * @param approverInfos
     * @throws InterruptedException
     */
    @Async
    public void sendSms(ActivityApprovalSmsInfo approvalSmsInfo, List<ApproverInfo> approverInfos) throws InterruptedException {
        for(ApproverInfo approverInfo : approverInfos){
            approvalSmsInfo.setApproverId(approverInfo.getApproverId());
            //approvalSmsInfo.setApproverLevel(approverInfo.getApproverLevel());
            approvalSmsInfo.setApproverName(approverInfo.getApproverName());
            approvalSmsInfo.setApproverPhone(approverInfo.getApproverPhone());

            //插入短信审批记录
            smsTaskService.insertSmsApproval(approvalSmsInfo);

            //发送短信
            String code = String.valueOf(approvalSmsInfo.getSmsCode());
            String result = smsHandler(SEND_URL,code,approverInfo.getApproverPhone(),approvalSmsInfo.getActivityContent());
            if (result.equals("true")) {
                approvalSmsInfo.setSmsSendState(1);
                logger.info("= = 审批短信srcId:{},发送号码:{},审批层级:{},发送成功!",code,approverInfo.getApproverPhone(),approvalSmsInfo.getApproverLevel());
            } else {
                approvalSmsInfo.setSmsSendState(0);
                //插入短信审批记录
                smsTaskService.insertSmsApproval(approvalSmsInfo);
                logger.info("= = 审批短信srcId:{},发送号码:{}，审批层级:{},发送失败!",code,approverInfo.getApproverPhone(),approvalSmsInfo.getApproverLevel());
            }

            //延迟3秒
            Thread.sleep(3000);
        }
    }

    /**
     * 执行下一级审批短信发送
     * @param map
     * @param approverInfos
     * @throws InterruptedException
     */
    @Async
    public void sendNextSms(Map map, List<ApproverInfo> approverInfos, int level) throws InterruptedException {
        String template = smsTaskService.getSmsTemplate(level);
        String  smsContent = "由"+map.get("CREATOR_NAME").toString()+"创建"
                +",活动名称:"+map.get("ACTIVITY_NAME").toString()+"("+map.get("ACTIVITY_ID")+")"
                +",归属地市:"+map.get("CITY_NAME").toString()
                +",执行渠道:"+map.get("CHANNEL_NAME").toString()
                +",目标用户数"+map.get("FINAL_AMOUNT").toString()
                +",活动内容:"+map.get("RULE_VALUE").toString()
                +",开始时间:"+map.get("START_TIME").toString()
                +",结束时间:"+map.get("END_TIME").toString();
        template = template.replaceAll("#_1",smsContent);
        if(level>1){
            Map parameterMap = new HashMap<>();
            parameterMap.put("activityId",map.get("ACTIVITY_ID").toString());
            parameterMap.put("channelId",map.get("CHANNEL_ID").toString());
            parameterMap.put("level",level-1);

            ApproverInfo approverInfo = smsTaskService.getPreApproverInfo(parameterMap);
            String temp = approverInfo.getApproverName()+"("+approverInfo.getApproverPhone()+")";
            template = template.replaceAll("#_1",smsContent).replaceAll("#_2",temp);
        }

        ActivityApprovalSmsInfo approvalSmsInfo = new ActivityApprovalSmsInfo();
        approvalSmsInfo.setActivityId(map.get("ACTIVITY_ID").toString());
        approvalSmsInfo.setActivityName(map.get("ACTIVITY_NAME").toString());
        approvalSmsInfo.setChannelId(map.get("CHANNEL_ID").toString());
        approvalSmsInfo.setChannelName(map.get("CHANNEL_NAME").toString());
        approvalSmsInfo.setActivityContent(template);
        approvalSmsInfo.setApproverLevel(level);

        sendSms(approvalSmsInfo,approverInfos);
    }

    /**
     * 读取短信审批回复
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    //@Scheduled(cron = "10 * * * * ?")
    public void getSmsReplyInfo()throws Exception{
        Map parameterMap = new HashMap<>();
        try{
            logger.info("= = = = = 定时任务：请求短信审批结果开始 = = = = =");
            List<ActivityApprovalSmsInfo> activityApprovalSmsInfoList = smsTaskService.getUnReplySmsApprovalInfo(validate);
            for(ActivityApprovalSmsInfo approvalSmsInfo : activityApprovalSmsInfoList){
                parameterMap.put("activityId",approvalSmsInfo.getActivityId());
                parameterMap.put("channelId",approvalSmsInfo.getChannelId());
                parameterMap.put("approverId",approvalSmsInfo.getApproverId());
                parameterMap.put("approverLevel",approvalSmsInfo.getApproverLevel());

                String smsCode = String.valueOf(approvalSmsInfo.getSmsCode());
                String tel = approvalSmsInfo.getApproverPhone();
                Map resultMap = new HashMap<>();
                resultMap.put("smsCode",smsCode);

                String reply = smsHandler(REPLY_URL,smsCode,tel,approvalSmsInfo.getActivityContent());
                reply = reply.toUpperCase();
                if( ! "NULL".equals(reply)){
                    ActivityApprovalInfo activityApprovalInfo = smsTaskService.getActivityApprovalResult(parameterMap);
                    boolean notApprovaled = activityApprovalInfo.getApprovalResult() == 0 ? true : false;
                    if(notApprovaled){//线上未审批


                        ActivityApproval activityApproval = new ActivityApproval();
                        activityApproval.setActivityId(approvalSmsInfo.getActivityId());
                        activityApproval.setActivityName(approvalSmsInfo.getActivityName());
                        ChannelApprovalDetail channelApprovalDetail = new ChannelApprovalDetail();
                        channelApprovalDetail.setChannelId(approvalSmsInfo.getChannelId());
                        channelApprovalDetail.setChannelName(approvalSmsInfo.getChannelName());
                        if("Y".equals(reply)){
                            resultMap.put("result",1);
                            channelApprovalDetail.setApprovalRsult(1);
                            logger.info("= = 活动：{},执行渠道：{},短信审批通过!",approvalSmsInfo.getActivityId(),approvalSmsInfo.getChannelName());
                        }else if("N".equals(reply)){
                            resultMap.put("result",2);
                            channelApprovalDetail.setApprovalRsult(2);
                            channelApprovalDetail.setApprovalComments("短信驳回");
                            logger.info("= = 活动：{},执行渠道：{},短信审批驳回!",approvalSmsInfo.getActivityId(),approvalSmsInfo.getChannelName());
                        }
                        List<ChannelApprovalDetail> channelApprovalDetailList = new ArrayList<>();
                        channelApprovalDetailList.add(channelApprovalDetail);
                        activityApproval.setChannelApprovalDetailList(channelApprovalDetailList);
                        User user = new User();
                        user.setUserId(approvalSmsInfo.getApproverId());

                        String msg = "全触点集中运营平台提醒：操作成功！";
                        smsHandler(SEND_URL,smsCode,tel,msg);

                        //更新活动状态 继续审批流程
                        activityService.approveActivityChannelInfo(activityApproval, user);
                        logger.info("短信审批处理结束，更新审批流程");

                        //更新短息审批记录表
                        smsTaskService.updateSmsApprovalInfo(resultMap);

                    }else{
                        String approvalResult  = activityApprovalInfo.getApprovalResult() == 1 ? "审批通过" : "驳回";
                        String smsContent = "全触点集中运营平台提醒：操作无效，该活动已由"+activityApprovalInfo.getApproverName()
                                +"("+activityApprovalInfo.getApproverPhone()+")" +approvalResult;
                        smsHandler(SEND_URL,smsCode,tel,smsContent);

                        resultMap.put("result",-1);
                        //更新短息审批记录表
                        smsTaskService.updateSmsApprovalInfo(resultMap);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  短信处理接口
     * @param url     短信请求链接
     * @param smsCode 短息发送号码
     * @param phoneNo 短信接收号码
     * @param message 短息内容
     * @return
     */
    @Async
    public String smsHandler(String url,String smsCode,String phoneNo,String message){
        Map sureMap = new HashMap<>();
        sureMap.put("touchCyc", "0");
        sureMap.put("channelType", "ZDXC");
        sureMap.put("msg",message);
        sureMap.put("tel", phoneNo);
        sureMap.put("srcId",SEND_CODE+smsCode);
        return HttpUtil.sendGet(url,sureMap);
    }



}
