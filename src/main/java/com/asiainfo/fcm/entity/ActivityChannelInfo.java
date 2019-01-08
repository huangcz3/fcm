package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 活动渠道信息
 */
public class ActivityChannelInfo {
    //活动ID
    private String activityId;
    //活动名称
    private String activityName;
    //渠道ID
    private String channelId;
    //渠道名称
    private String channelName;
    //渠道规则ID
    private String ruleId;
    //渠道规则值
    private String ruleValue;
    //渠道规则扩展值
    private String ruleExtendedValue;

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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleExtendedValue() {
        return ruleExtendedValue;
    }

    public void setRuleExtendedValue(String ruleExtendedValue) {
        this.ruleExtendedValue = ruleExtendedValue;
    }
}
