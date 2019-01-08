package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/7/4/0004.
 */
public class ActivityApprovalInfo {
    //活动id
    private String activityId;
    //活动名称
    private String activityName;
    //渠道id
    private String channelId;
    //渠道名称
    private String channelName;
    //审批结果：0未审批，1审批通过，2审
    private int approvalResult;
    //审批意见
    private String approvalComments;
    //审批人id
    private String approverId;
    //审批人姓名
    private String approverName;
    //审批人手机号
    private String approverPhone;
    //审批人层级
    private int approverLevel;
    //审批时间
    private String approveTime;
    //审批组
    private String approvalGroup;
    //审批角色
    private String approvalRole;


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

    public int getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(int approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
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

    public String getApproverPhone() {
        return approverPhone;
    }

    public void setApproverPhone(String approverPhone) {
        this.approverPhone = approverPhone;
    }

    public int getApproverLevel() {
        return approverLevel;
    }

    public void setApproverLevel(int approverLevel) {
        this.approverLevel = approverLevel;
    }

    public String getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(String approveTime) {
        this.approveTime = approveTime;
    }

    public String getApprovalGroup() {
        return approvalGroup;
    }

    public void setApprovalGroup(String approvalGroup) {
        this.approvalGroup = approvalGroup;
    }

    public String getApprovalRole() {
        return approvalRole;
    }

    public void setApprovalRole(String approvalRole) {
        this.approvalRole = approvalRole;
    }
}
