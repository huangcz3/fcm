package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 活动客户群信息
 */
public class ActivityCustomerGroupInfo {
    //活动ID
    private String activityId;
    //活动名称
    private String activityName;
    //客户群ID
    private String customerGroupId;
    //客户群名称
    private String customerGroupName;
    //创建方式：1标签库，2数据集市，3文件上传，4数字内容
    private int createType;

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

    public String getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(String customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public String getCustomerGroupName() {
        return customerGroupName;
    }

    public void setCustomerGroupName(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

    public int getCreateType() {
        return createType;
    }

    public void setCreateType(int createType) {
        this.createType = createType;
    }
}
