package com.asiainfo.fcm.entity;

/**
 * Created by Administrator on 2017/9/20.
 */
public class IopSendOrder {
    //下发主活动ID
    private String activityId;
    //下发子活动ID
    private String campaignId;
    //被派人手机号
    private String executorPhone;
    //指派人手机号
    private String commanderPhone;
    //本地活动ID
    private String campId;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getExecutorPhone() {
        return executorPhone;
    }

    public void setExecutorPhone(String executorPhone) {
        this.executorPhone = executorPhone;
    }

    public String getCommanderPhone() {
        return commanderPhone;
    }

    public void setCommanderPhone(String commanderPhone) {
        this.commanderPhone = commanderPhone;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }
}
