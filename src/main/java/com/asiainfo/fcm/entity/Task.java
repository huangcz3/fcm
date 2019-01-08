package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by RUOK on 2017/7/5.
 */
public class Task {
    private String taskId;

    private String taskName;

    private String creatorId;

    private String creatorName;

    private String createTime;

    private String startTime;

    private String endTime;

    private String cityId;

    private String cityName;

    //业务类型id
    private int businessTypeId;
    //业务类型名
    private String businessTypeName;
    //营销目的id
    private int marketingPurposeId;
    //营销目的名
    private String marketingPurposeName;

    private List<Activity> activityList;

    private int effective;

    //1:可删除 ； 0：不可删除
    private int isCanDelete;

    private int relativeActivityNum;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public int getMarketingPurposeId() {
        return marketingPurposeId;
    }

    public void setMarketingPurposeId(int marketingPurposeId) {
        this.marketingPurposeId = marketingPurposeId;
    }

    public String getMarketingPurposeName() {
        return marketingPurposeName;
    }

    public void setMarketingPurposeName(String marketingPurposeName) {
        this.marketingPurposeName = marketingPurposeName;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }

    public int getIsCanDelete() {
        return isCanDelete;
    }

    public void setIsCanDelete(int isCanDelete) {
        this.isCanDelete = isCanDelete;
    }

    public int getRelativeActivityNum() {
        return relativeActivityNum;
    }

    public void setRelativeActivityNum(int relativeActivityNum) {
        this.relativeActivityNum = relativeActivityNum;
    }
}
