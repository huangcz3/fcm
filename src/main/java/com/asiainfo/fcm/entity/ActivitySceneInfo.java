package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/7/26/0026.
 * 活动场景信息表
 */
public class ActivitySceneInfo {
    //活动id
    private String activityId;
    //活动名称
    private String activityName;
    //场景id
    private int sceneId;
    //场景名称
    private String sceneName;
    //场景规则id
    private String sceneRuleId;
    //场景规则值
    private String sceneRuleValue;
    //场景规则扩展值
    private String sceneRuleExtendedValue;

    //场景规则名称
    private String sceneRuleName;

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

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneRuleId() {
        return sceneRuleId;
    }

    public void setSceneRuleId(String sceneRuleId) {
        this.sceneRuleId = sceneRuleId;
    }

    public String getSceneRuleValue() {
        return sceneRuleValue;
    }

    public void setSceneRuleValue(String sceneRuleValue) {
        this.sceneRuleValue = sceneRuleValue;
    }

    public String getSceneRuleExtendedValue() {
        return sceneRuleExtendedValue;
    }

    public void setSceneRuleExtendedValue(String sceneRuleExtendedValue) {
        this.sceneRuleExtendedValue = sceneRuleExtendedValue;
    }

    public String getSceneRuleName() {
        return sceneRuleName;
    }

    public void setSceneRuleName(String sceneRuleName) {
        this.sceneRuleName = sceneRuleName;
    }
}
