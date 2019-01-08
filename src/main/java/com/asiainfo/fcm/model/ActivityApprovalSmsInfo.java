package com.asiainfo.fcm.model;

/**
 * Created by PuMg on 2017/7/28/0028.
 * 短信审批记录表信息
 */
public class ActivityApprovalSmsInfo {
    //短信唯一发送码
    private int smsCode;

    private String activityId;

    private String activityName;

    private String channelId;

    private String channelName;
    //活动内容
    private String activityContent;

    private String approverId;

    private String approverName;

    private int approverLevel;

    private String approverPhone;

    //短信审批结果 0未审批 1审批通过 2审批驳回
    private String smsApprovalResult;
    //短息发送时间
    private String smsSendTime;
    //短息发送状态 0未发送 1已发送
    private int smsSendState;

    public int getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(int smsCode) {
        this.smsCode = smsCode;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public int getApproverLevel() {
        return approverLevel;
    }

    public void setApproverLevel(int approverLevel) {
        this.approverLevel = approverLevel;
    }

    public String getApproverPhone() {
        return approverPhone;
    }

    public void setApproverPhone(String approverPhone) {
        this.approverPhone = approverPhone;
    }

    public String getSmsApprovalResult() {
        return smsApprovalResult;
    }

    public void setSmsApprovalResult(String smsApprovalResult) {
        this.smsApprovalResult = smsApprovalResult;
    }

    public String getSmsSendTime() {
        return smsSendTime;
    }

    public void setSmsSendTime(String smsSendTime) {
        this.smsSendTime = smsSendTime;
    }

    public int getSmsSendState() {
        return smsSendState;
    }

    public void setSmsSendState(int smsSendState) {
        this.smsSendState = smsSendState;
    }
}
