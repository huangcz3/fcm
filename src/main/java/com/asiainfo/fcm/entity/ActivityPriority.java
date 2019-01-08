package com.asiainfo.fcm.entity;

/**
 * 活动优先级信息表.
 * Created by PuMg on 2017/6/26/0026.
 */
public class ActivityPriority {
    //活动ID
    private String activityId;
    //活动名称
    private String activityName;
    //活动地市ID
    private String cityId;
    //活动地市名称
    private String cityName;
    //优先级设置时间（值越大优先级越高，新建默认创建时间）
    private String priorityTime;

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

    public String getPriorityTime() {
        return priorityTime;
    }

    public void setPriorityTime(String priorityTime) {
        this.priorityTime = priorityTime;
    }
}
