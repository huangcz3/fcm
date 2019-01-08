package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 渠道信息表
 */
public class ChannelInfo {

    //渠道id
    private String channelId;

    //渠道名称
    private String channelName;

    //渠道类型id
    private int channelTypeId;

    //渠道类型名称
    private String channelTypeName;

    //是否需要内容审批：0不需要，1需要
    private int needContentApproval;

    //是否需要渠道审批：0不需要，1需要
    private int needChannelApproval;

    //是否需要分管领导审批
    private int needLeaderApproval;

    //是否支持短息审批：0 不支持 1支持
    private int isCanSmsApproval;

    //是否有效：0无效，1有效
    private int effective;

    //渠道审批人
    private List<ApproverInfo> approverInfoList;

    //渠道规则信息
    private List<ChannelRule> channelRuleList;

    public List<ChannelRule> getChannelRuleList() {
        return channelRuleList;
    }

    public int getNeedLeaderApproval() {
        return needLeaderApproval;
    }

    public void setNeedLeaderApproval(int needLeaderApproval) {
        this.needLeaderApproval = needLeaderApproval;
    }

    public void setChannelRuleList(List<ChannelRule> channelRuleList) {
        this.channelRuleList = channelRuleList;
    }

    public List<ApproverInfo> getApproverInfoList() {
        return approverInfoList;
    }

    public void setApproverInfoList(List<ApproverInfo> approverInfoList) {
        this.approverInfoList = approverInfoList;
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

    public int getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(int channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getChannelTypeName() {
        return channelTypeName;
    }

    public void setChannelTypeName(String channelTypeName) {
        this.channelTypeName = channelTypeName;
    }

    public int getNeedContentApproval() {
        return needContentApproval;
    }

    public void setNeedContentApproval(int needContentApproval) {
        this.needContentApproval = needContentApproval;
    }

    public int getNeedChannelApproval() {
        return needChannelApproval;
    }

    public void setNeedChannelApproval(int needChannelApproval) {
        this.needChannelApproval = needChannelApproval;
    }

    public int getIsCanSmsApproval() {
        return isCanSmsApproval;
    }

    public void setIsCanSmsApproval(int isCanSmsApproval) {
        this.isCanSmsApproval = isCanSmsApproval;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }
}
