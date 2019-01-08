package com.asiainfo.fcm.model;

import com.asiainfo.fcm.entity.ApproverInfo;

import java.util.List;

/**
 * Created by PuMg on 2017/7/6/0006.
 */
public class ActivityChannelDetail {
    //活动ID
    private String activityId;
    //活动名称
    private String activityName;
    //渠道ID
    private String channelId;
    //渠道名称
    private String channelName;
    //审批人
    private List<ApproverInfo> approverInfoList;
    //渠道规则明细
    List<ActivityChannelRuleDetail> activityChannelRuleDetailList;

    List<ChannelDetail> channelDetailList ;

    public List<ChannelDetail> getChannelDetailList() {
        return channelDetailList;
    }

    public void setChannelDetailList(List<ChannelDetail> channelDetailList) {
        this.channelDetailList = channelDetailList;
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
}
