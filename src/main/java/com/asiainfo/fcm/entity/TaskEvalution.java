package com.asiainfo.fcm.entity;

/**
 * Created by Administrator on 2017/11/6.
 */
public class TaskEvalution {
    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    private String cityId;

    private String cityName;

    /**
     * 本月目标客户
     */
    private String targetNum;

    /**
     * 本期接触量
     */
    private int touchNum;
    /**
     * 接触率
     */
    private String touchRate;
    /**
     * 业务办理成功量
     */
    private int vicNum;

    /**
     * 成功订购率
     */
    private String vicRate;
    /**
     * 关联活动个数
     */
    private int relatedNum;

    /**
     * 时间
     * @return
     */
    private String opTime;

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

    public String getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(String targetNum) {
        this.targetNum = targetNum;
    }

    public int getTouchNum() {
        return touchNum;
    }

    public void setTouchNum(int touchNum) {
        this.touchNum = touchNum;
    }

    public String getTouchRate() {
        return touchRate;
    }

    public void setTouchRate(String touchRate) {
        this.touchRate = touchRate;
    }

    public int getVicNum() {
        return vicNum;
    }

    public void setVicNum(int vicNum) {
        this.vicNum = vicNum;
    }

    public String getVicRate() {
        return vicRate;
    }

    public void setVicRate(String vicRate) {
        this.vicRate = vicRate;
    }

    public int getRelatedNum() {
        return relatedNum;
    }

    public void setRelatedNum(int relatedNum) {
        this.relatedNum = relatedNum;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }
}
