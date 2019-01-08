package com.asiainfo.fcm.entity;

import com.asiainfo.fcm.model.ActivityChannelDetail;

import java.util.List;

/**
 * Created by PuMg on 2017/6/26/0026.
 */
public class Activity {

    //活动id
    private String activityId;
    //活动名称
    private String activityName;
    //创建人工号
    private String creatorId;
    //创建人姓名
    private String creatorName;
    //创建时间
    private String createTime;
    //活动生效时间
    private String startTime;
    //活动失效时间
    private String endTime;
    //归属地市id
    private String cityId;
    //归属地市名
    private String cityName;
    //业务类型id
    private int businessTypeId;
    //业务类型名
    private String businessTypeName;
    //业务小类型id
    private int businessSmallTypeId;
    //业务小类型名
    private String businessSmallTypeName;
    //营销目的id
    private int marketingPurposeId;
    //营销目的名
    private String marketingPurposeName;
    //是否场景活动
    private int sceneFlag;
    //场景id
    private int sceneId;
    //场景名
    private String sceneName;
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
    /**
     * 活动状态：0待生成目标用户，1目标用户生成成功，待内容审批，2目标用户生成失败，
     * 3目标用户生成成功但超过数量限制，4内容审批通过，5内容审批驳回，6渠道审批通过，
     * 7渠道审批驳回，8活动执行中，9结束
     */
    private int activityState;
    //是否主动终止：0否，1是
    private int stopped;
    //是否可删除
    private int deleted;
    //客户群更新周期：1一次性，2月周期，3日周期
    private int customerUpdateCycle;

    //归属部门
    private String deptId;
    //归属部门名称
    private String deptName;

    /**
     * 活动渠道信息
     */
    private List<ActivityChannelDetail> activityChannelDetailList;

    /**
     * 活动推荐业务信息
     */
    private List<ActivityRecommendProduct> activityRecommendProducts;

    /**
     * 测试号码（以逗号分隔）
     */
    private String testPhoneNum;

    /**
     * 是否可以审批
     */
    private String isCanApproval;

    /**
     * 是否可以删除 0 不可以删除 1 可以删除
     */
    private int isCanDelete;
    /**
     * 用户群信息
     */
    private List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList;

    /**
     * 目标用户数
     */
    private long finalAmount;

    /**
     * 目标用户生成明细
     */
    private CustomerRemoveInfo customerRemoveInfo;

    /**
     * 活动场景信息
     */
    private List<ActivitySceneInfo> activitySceneInfoList;

    public List<ActivitySceneInfo> getActivitySceneInfoList() {
        return activitySceneInfoList;
    }

    public void setActivitySceneInfoList(List<ActivitySceneInfo> activitySceneInfoList) {
        this.activitySceneInfoList = activitySceneInfoList;
    }

    public CustomerRemoveInfo getCustomerRemoveInfo() {
        return customerRemoveInfo;
    }

    public void setCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo) {
        this.customerRemoveInfo = customerRemoveInfo;
    }

    public long getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(long finalAmount) {
        this.finalAmount = finalAmount;
    }

    public List<ActivityCustomerGroupInfo> getActivityCustomerGroupInfoList() {
        return activityCustomerGroupInfoList;
    }

    public void setActivityCustomerGroupInfoList(List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList) {
        this.activityCustomerGroupInfoList = activityCustomerGroupInfoList;
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

    public int getSceneFlag() {
        return sceneFlag;
    }

    public void setSceneFlag(int sceneFlag) {
        this.sceneFlag = sceneFlag;
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

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    public int getStopped() {
        return stopped;
    }

    public void setStopped(int stopped) {
        this.stopped = stopped;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getCustomerUpdateCycle() {
        return customerUpdateCycle;
    }

    public void setCustomerUpdateCycle(int customerUpdateCycle) {
        this.customerUpdateCycle = customerUpdateCycle;
    }

    public List<ActivityChannelDetail> getActivityChannelDetailList() {
        return activityChannelDetailList;
    }

    public void setActivityChannelDetailList(List<ActivityChannelDetail> activityChannelDetailList) {
        this.activityChannelDetailList = activityChannelDetailList;
    }

    public List<ActivityRecommendProduct> getActivityRecommendProducts() {
        return activityRecommendProducts;
    }

    public void setActivityRecommendProducts(List<ActivityRecommendProduct> activityRecommendProducts) {
        this.activityRecommendProducts = activityRecommendProducts;
    }

    public String getTestPhoneNum() {
        return testPhoneNum;
    }

    public void setTestPhoneNum(String testPhoneNum) {
        this.testPhoneNum = testPhoneNum;
    }

    public String getIsCanApproval() {
        return isCanApproval;
    }

    public void setIsCanApproval(String isCanApproval) {
        this.isCanApproval = isCanApproval;
    }

    public int getIsCanDelete() {
        return isCanDelete;
    }

    public void setIsCanDelete(int isCanDelete) {
        this.isCanDelete = isCanDelete;
    }

    public int getBusinessSmallTypeId() {
        return businessSmallTypeId;
    }

    public void setBusinessSmallTypeId(int businessSmallTypeId) {
        this.businessSmallTypeId = businessSmallTypeId;
    }

    public String getBusinessSmallTypeName() {
        return businessSmallTypeName;
    }

    public void setBusinessSmallTypeName(String businessSmallTypeName) {
        this.businessSmallTypeName = businessSmallTypeName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
