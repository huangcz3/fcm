package com.asiainfo.fcm.task;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivityApprovalInfo;
import com.asiainfo.fcm.entity.ActivityChannelInfo;
import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.model.ActivityApprovalSmsInfo;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ISMSTaskService;
import com.asiainfo.fcm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/7/28/0028.
 * 短信审批定时任务
 */
//@Component
public class SMSApprovalTask {
    private Logger logger = LoggerFactory.getLogger(SMSApprovalTask.class);

    @Autowired
    private ISMSTaskService smsTaskService;

    @Autowired
    private IActivityService activityService;

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
     * 扫描短信审批配置信息
     * 读取未审批活动信息，插入短信审批记录表
     * 发送短信
     */
    //@Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "0 0/5 9,10,11,12,14,15,16,17,18,19,20 * * *")
    public void sendSMSApprovalInfo()throws Exception{
        List<String> strings = smsTaskService.getNeedSmsChannels();
        if(strings.size()>0){
            List<ActivityApprovalInfo> activityApprovalInfoList = smsTaskService.getSMSApprovalInfo(strings);
            logger.info("= = = = = 定时任务执行 = = = = =");
            for (ActivityApprovalInfo activityApprovalInfo :activityApprovalInfoList){
                //判断是否已发送过审批短息
                Map map = new HashMap<>();
                map.put("activityId",activityApprovalInfo.getActivityId());
                map.put("channelId",activityApprovalInfo.getChannelId());
                map.put("approverId",activityApprovalInfo.getApproverId());
                map.put("approverPhone",activityApprovalInfo.getApproverPhone());
                map.put("approverLevel",activityApprovalInfo.getApproverLevel());
                map.put("sendInterval",sendInterval);

                int isSend = smsTaskService.isSendedSms(map);
                if(isSend ==0 ) {
                    String smsContent = builSmsContent(activityApprovalInfo);
                    logger.info("短息发送内容：" + smsContent);
                    ActivityApprovalSmsInfo approvalSmsInfo = new ActivityApprovalSmsInfo();
                    approvalSmsInfo.setActivityId(activityApprovalInfo.getActivityId());
                    approvalSmsInfo.setActivityName(activityApprovalInfo.getActivityName());
                    approvalSmsInfo.setChannelId(activityApprovalInfo.getChannelId());
                    approvalSmsInfo.setChannelName(activityApprovalInfo.getChannelName());
                    approvalSmsInfo.setActivityContent(smsContent);
                    approvalSmsInfo.setApproverId(activityApprovalInfo.getApproverId());
                    approvalSmsInfo.setApproverLevel(activityApprovalInfo.getApproverLevel());
                    approvalSmsInfo.setApproverName(activityApprovalInfo.getApproverName());
                    approvalSmsInfo.setApproverPhone(activityApprovalInfo.getApproverPhone());

                    //插入短信审批记录
                    smsTaskService.insertSmsApproval(approvalSmsInfo);

                    //发送短信
                    Map<String, String> parameterMap = new HashMap<>();
                    parameterMap.put("touchCyc", "0");
                    parameterMap.put("channelType", "ZDXC");
                    parameterMap.put("msg", smsContent);
                    parameterMap.put("tel", activityApprovalInfo.getApproverPhone());
                    parameterMap.put("srcId", SEND_CODE +approvalSmsInfo.getSmsCode() );

//                    HttpUtil httpUtil = new HttpUtil();
                    String result = HttpUtil.sendGet(SEND_URL, parameterMap);
                    if (result.equals("true")) {
                        approvalSmsInfo.setSmsSendState(1);
                        logger.info("= = = = = 发送短信："+parameterMap.get("srcId")+",发送号码："+activityApprovalInfo.getApproverPhone()+" 成功 = = = = =");
                    } else {
                        approvalSmsInfo.setSmsSendState(0);
                        logger.info("= = = = = 发送短信："+parameterMap.get("srcId")+",发送号码："+activityApprovalInfo.getApproverPhone()+" 失败 = = = = =");
                    }
                    //延迟3秒
                    Thread.sleep(3000);
                }
            }
        }
    }

    /**
     * 封装活动信息 插入短息审批记录表
     */
    public String builSmsContent(ActivityApprovalInfo activityApprovalInfo){
        //获取短信模板
        String template = smsTaskService.getSmsTemplate(activityApprovalInfo.getApproverLevel());
        String smsContent = "";
        Activity activity = activityService.getActivityDetailInfo(activityApprovalInfo.getActivityId());
        ActivityChannelInfo activityChannelInfo = smsTaskService.getActivityChannelInfo(activityApprovalInfo.getActivityId());

        int level = activityApprovalInfo.getApproverLevel();
        Map parameterMap = new HashMap<>();
        parameterMap.put("activityId",activityApprovalInfo.getActivityId());
        parameterMap.put("channelId",activityApprovalInfo.getChannelId());

        if( level == 1){
            parameterMap.put("level",level);
            ApproverInfo approverInfo = smsTaskService.getPreApproverInfo(parameterMap);
            smsContent = "活动名称:"+activity.getActivityName()+"("+activity.getActivityId()+")"
                    +",归属地市:"+activity.getCityName()
                    +",创建人:"+activity.getCreatorName()+"("+approverInfo.getApproverPhone()+")"
                    +",执行渠道:"+activityApprovalInfo.getChannelName()
                    +",活动内容:"+activityChannelInfo.getRuleValue()
                    +",开始时间:"+activity.getStartTime()
                    +",结束时间:"+activity.getEndTime();

            template = template.replaceAll("#_1",smsContent);
        }else{
            parameterMap.put("level",level-1);
            ApproverInfo approverInfo = smsTaskService.getPreApproverInfo(parameterMap);
            smsContent = "活动名称:"+activity.getActivityName()+"("+activity.getActivityId()+")"
                    +",执行渠道:"+activityApprovalInfo.getChannelName()
                    +",归属地市:"+activity.getCityName();
            String temp = approverInfo.getApproverName()+"("+approverInfo.getApproverPhone()+")";
            template = template.replaceAll("#_1",smsContent).replaceAll("#_2",temp);
        }
        return template;
    }




}
