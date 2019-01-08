package com.asiainfo.fcm.model;

import java.util.List;

/**
 * Created by PuMg on 2017/7/5/0005.
 * 活动审批上传对象
 */
public class ActivityApproval {
    //活动ID
    private String activityId;
    //活动名称
    private String activityName;
    //渠道审批信息
    List<ChannelApprovalDetail> channelApprovalDetailList;

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

    public List<ChannelApprovalDetail> getChannelApprovalDetailList() {
        return channelApprovalDetailList;
    }

    public void setChannelApprovalDetailList(List<ChannelApprovalDetail> channelApprovalDetailList) {
        this.channelApprovalDetailList = channelApprovalDetailList;
    }
}
