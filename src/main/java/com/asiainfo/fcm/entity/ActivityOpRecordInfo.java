package com.asiainfo.fcm.entity;

/**
 * Created by chengzhongtao on 2017/12/15.
 */
public class ActivityOpRecordInfo {
    //活动id
    private String activityId;
    //用户id
    private String userId;
    //用户名
    private String userName;
    //用户电话
    private String userPhone;
    //操作类型：1、活动 2、调度
    private int opType;
    //操作编码
    private String opCode;
    //操作名称
    private String opName;
    //操作说明
    private String opDesc;
    //操作结果 1、成功 0、失败
    private int opResult;
    //操作时间
    private String opTime;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpDesc() {
        return opDesc;
    }

    public void setOpDesc(String opDesc) {
        this.opDesc = opDesc;
    }

    public int getOpResult() {
        return opResult;
    }

    public void setOpResult(int opResult) {
        this.opResult = opResult;
    }

    public String getOpTime() {return opTime;}

    public void setOpTime(String opTime) {this.opTime = opTime;}
}
