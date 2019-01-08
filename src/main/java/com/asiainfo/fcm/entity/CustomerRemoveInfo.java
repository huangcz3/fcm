package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 活动目标用户生成信息
 */
public class CustomerRemoveInfo {
    //活动id
    private String activityId;
    //活动名称
    private String activityName;
    //原始客户数
    private long originalAmount;
    //是否剔除内部员工：0否，1是
    private int removeEmployee;
    //剔除内部员工数
    private long removeEmployeeAmount;
    //是否剔除红名单：0否，1是
    private int removeRedList;
    //剔除红名单用户数
    private long removeRedListAmount;
    //是否剔除敏感用户：0否，1是
    private int removeSensitive;
    //是否剔除敏感用户数
    private long removeSensitiveAmount;
    //是否剔除取消10086流量提醒用户：0否，1是
    private int removeCancel10086;
    //是否剔除取消10086流量提醒用户数
    private long removeCancel10086Amount;
    //是否剔除集团重要客户：0否，1是
    private int removeGroupUser;
    //是否剔除集团重要客户数
    private long removeGroupUserAmount;
    //是否剔除自定义用户：0否，1是
    private int removeUpload;
    //是否剔除自定义用户数
    private long removeUploadAmount;
    //是否自定义营销语：0否，1是
    private int customizeFlag;
    //最终客户群表名
    private String finalGroupTableName;
    //最终用户数
    private long finalAmount;
    //目标用户生成状态：0未生成，1生成成功，2生成失败,3后台处理成功，4后台处理失败
    private int state;
    //异常信息
    private String exceptionInfo;

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

    public long getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(long originalAmount) {
        this.originalAmount = originalAmount;
    }

    public int getRemoveEmployee() {
        return removeEmployee;
    }

    public void setRemoveEmployee(int removeEmployee) {
        this.removeEmployee = removeEmployee;
    }

    public long getRemoveEmployeeAmount() {
        return removeEmployeeAmount;
    }

    public void setRemoveEmployeeAmount(long removeEmployeeAmount) {
        this.removeEmployeeAmount = removeEmployeeAmount;
    }

    public int getRemoveRedList() {
        return removeRedList;
    }

    public void setRemoveRedList(int removeRedList) {
        this.removeRedList = removeRedList;
    }

    public long getRemoveRedListAmount() {
        return removeRedListAmount;
    }

    public void setRemoveRedListAmount(long removeRedListAmount) {
        this.removeRedListAmount = removeRedListAmount;
    }

    public int getRemoveSensitive() {
        return removeSensitive;
    }

    public void setRemoveSensitive(int removeSensitive) {
        this.removeSensitive = removeSensitive;
    }

    public long getRemoveSensitiveAmount() {
        return removeSensitiveAmount;
    }

    public void setRemoveSensitiveAmount(long removeSensitiveAmount) {
        this.removeSensitiveAmount = removeSensitiveAmount;
    }

    public int getRemoveCancel10086() {
        return removeCancel10086;
    }

    public void setRemoveCancel10086(int removeCancel10086) {
        this.removeCancel10086 = removeCancel10086;
    }

    public long getRemoveCancel10086Amount() {
        return removeCancel10086Amount;
    }

    public void setRemoveCancel10086Amount(long removeCancel10086Amount) {
        this.removeCancel10086Amount = removeCancel10086Amount;
    }

    public int getRemoveGroupUser() {
        return removeGroupUser;
    }

    public void setRemoveGroupUser(int removeGroupUser) {
        this.removeGroupUser = removeGroupUser;
    }

    public long getRemoveGroupUserAmount() {
        return removeGroupUserAmount;
    }

    public void setRemoveGroupUserAmount(long removeGroupUserAmount) {
        this.removeGroupUserAmount = removeGroupUserAmount;
    }

    public int getRemoveUpload() {
        return removeUpload;
    }

    public void setRemoveUpload(int removeUpload) {
        this.removeUpload = removeUpload;
    }

    public long getRemoveUploadAmount() {
        return removeUploadAmount;
    }

    public void setRemoveUploadAmount(long removeUploadAmount) {
        this.removeUploadAmount = removeUploadAmount;
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

    public long getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(long finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }
}
