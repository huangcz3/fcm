package com.asiainfo.fcm.model;

/**
 * Created by PuMg on 2017/8/14/0014.
 * 上网行为信息
 */
public class OnlineBehaviorInfo {
    private String activityId;

    private String effTime;

    private String expTime;

    private int status;

    private String sceneValue;

    private String content;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getEffTime() {
        return effTime;
    }

    public void setEffTime(String effTime) {
        this.effTime = effTime;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSceneValue() {
        return sceneValue;
    }

    public void setSceneValue(String sceneValue) {
        this.sceneValue = sceneValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
