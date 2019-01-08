package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 策略库--运营场景；kpiBO效果评估指标类对象
 */
public class PolicySceneKpiBO {
    private String activityId="";

    private String cycle="";

    /**
     * 通用指标类
     */
    private String  commonKpiMap="";
    /**
     * PCC类指标；暂无
     */
    private String pccKpiMap="";
    /**
     * 其他业务类指标；暂无
     */
    private String otherKpiMap="";

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getCommonKpiMap() {
        return commonKpiMap;
    }

    public void setCommonKpiMap(String commonKpiMap) {
        this.commonKpiMap = commonKpiMap;
    }

    public String getPccKpiMap() {
        return pccKpiMap;
    }

    public void setPccKpiMap(String pccKpiMap) {
        this.pccKpiMap = pccKpiMap;
    }

    public String getOtherKpiMap() {
        return otherKpiMap;
    }

    public void setOtherKpiMap(String otherKpiMap) {
        this.otherKpiMap = otherKpiMap;
    }
}
