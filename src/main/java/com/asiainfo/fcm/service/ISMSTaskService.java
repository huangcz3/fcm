package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.ActivityApprovalInfo;
import com.asiainfo.fcm.entity.ActivityChannelInfo;
import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.model.ActivityApprovalSmsInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/7/28/0028.
 */
public interface ISMSTaskService {

    //获取短信审批配置信息
    List<String> getNeedSmsChannels();

    //查询活动信息
    List<ActivityApprovalInfo> getSMSApprovalInfo(List<String> channelIds);

    //获取短息发送模板
    String getSmsTemplate(int level);

    //插入短信审批记录表
    void insertSmsApproval(ActivityApprovalSmsInfo approvalSmsInfo);

    //是否已发送过短信
    int isSendedSms(Map map);

    List<ActivityApprovalSmsInfo> getUnReplySmsApprovalInfo(int validate);

    //更新记录表状态
    void updateSmsApprovalInfo(Map map);

    //获取当前活动审批状态
    ActivityApprovalInfo getActivityApprovalResult(Map map);

    //获取上一级审批人信息
    ApproverInfo getPreApproverInfo(Map map);

    //活动渠道信息
    ActivityChannelInfo getActivityChannelInfo(String activityId);

    //是否支持短信审批
    int isCanSmsApproval(Map map);
}
