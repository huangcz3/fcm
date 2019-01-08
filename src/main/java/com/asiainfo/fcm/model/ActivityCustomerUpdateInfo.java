package com.asiainfo.fcm.model;

import com.asiainfo.fcm.entity.CustomerGroupInfo;

import java.util.List;

public class ActivityCustomerUpdateInfo {
    //活动id
    private String activityId;
    //创建人id
    private String creatorId;
    //创建人姓名
    private String creatorName;
    //是否剔除内部号码：0否，1是
    private int removeEmployee;
    //是否剔除红名单：0否，1是
    private int removeRedList;
    //是否剔除敏感用户：0否，1是
    private int removeSensitive;
    //是否剔除取消10086流量提醒用户：0否，1是
    private int removeCancel10086;
    //是否剔除集团重要客户：0否，1是
    private int removeGroupUser;
    //是否剔除自定义上传用户：0否，1是
    private int removeUpload;
    //自定义剔除客户群id
    private String removeCustomerGroupId;
    //是否自定义营销语
    private int customizeFlag;
    //活动最终用户表名
    private String finalGroupTableName;
    //客户群列表
    private List<CustomerGroupInfo> customerGroupInfoList;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public int getRemoveEmployee() {
        return removeEmployee;
    }

    public void setRemoveEmployee(int removeEmployee) {
        this.removeEmployee = removeEmployee;
    }

    public int getRemoveRedList() {
        return removeRedList;
    }

    public void setRemoveRedList(int removeRedList) {
        this.removeRedList = removeRedList;
    }

    public int getRemoveSensitive() {
        return removeSensitive;
    }

    public void setRemoveSensitive(int removeSensitive) {
        this.removeSensitive = removeSensitive;
    }

    public int getRemoveCancel10086() {
        return removeCancel10086;
    }

    public void setRemoveCancel10086(int removeCancel10086) {
        this.removeCancel10086 = removeCancel10086;
    }

    public int getRemoveGroupUser() {
        return removeGroupUser;
    }

    public void setRemoveGroupUser(int removeGroupUser) {
        this.removeGroupUser = removeGroupUser;
    }

    public int getRemoveUpload() {
        return removeUpload;
    }

    public void setRemoveUpload(int removeUpload) {
        this.removeUpload = removeUpload;
    }

    public String getRemoveCustomerGroupId() {
        return removeCustomerGroupId;
    }

    public void setRemoveCustomerGroupId(String removeCustomerGroupId) {
        this.removeCustomerGroupId = removeCustomerGroupId;
    }

    public int getCustomizeFlag() {
        return customizeFlag;
    }

    public void setCustomizeFlag(int customizeFlag) {
        this.customizeFlag = customizeFlag;
    }

    public String getFinalGroupTableName() {
        return finalGroupTableName;
    }

    public void setFinalGroupTableName(String finalGroupTableName) {
        this.finalGroupTableName = finalGroupTableName;
    }

    public List<CustomerGroupInfo> getCustomerGroupInfoList() {
        return customerGroupInfoList;
    }

    public void setCustomerGroupInfoList(List<CustomerGroupInfo> customerGroupInfoList) {
        this.customerGroupInfoList = customerGroupInfoList;
    }
}
