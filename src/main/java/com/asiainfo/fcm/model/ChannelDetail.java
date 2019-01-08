package com.asiainfo.fcm.model;

import com.asiainfo.fcm.entity.ApproverInfo;

import java.util.List;

/**
 * Created by PuMg on 2017/7/10/0010.
 */
public class ChannelDetail {
    private String channelId;
    private String channelName;
    //审批人
    private List<ApproverInfo> approverInfoList;
    //渠道规则明细
    List<ActivityChannelRuleDetail> activityChannelRuleDetailList;

    public List<ApproverInfo> getApproverInfoList() {
        return approverInfoList;
    }

    public void setApproverInfoList(List<ApproverInfo> approverInfoList) {
        this.approverInfoList = approverInfoList;
    }

    public List<ActivityChannelRuleDetail> getActivityChannelRuleDetailList() {
        return activityChannelRuleDetailList;
    }

    public void setActivityChannelRuleDetailList(List<ActivityChannelRuleDetail> activityChannelRuleDetailList) {
        this.activityChannelRuleDetailList = activityChannelRuleDetailList;
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


}
