package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 审批人配置表
 */
public class ApproverInfo {

    //主键id
    private int uuid;

    //渠道id
    private String channelId;

    //渠道名称
    private String channelName;

    //地市id
    private String cityId;

    //地市名称
    private String cityName;

    //审批人id
    private String approverId;

    //修改审批人时的原始id
    private String originalApproverId;

    //审批人姓名
    private String approverName;

    //审批人手机
    private String approverPhone;

    //审批人所属群组
    private String approvalGroup;

    //审批人角色
    private String approvalRole;

    //审批人层级
    private int approverLevel;

    //是否有效：0无效，1有效
    private int effective;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public String getOriginalApproverId() {
        return originalApproverId;
    }

    public void setOriginalApproverId(String originalApproverId) {
        this.originalApproverId = originalApproverId;
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

    public int getApproverLevel() {
        return approverLevel;
    }

    public void setApproverLevel(int approverLevel) {
        this.approverLevel = approverLevel;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }
}
