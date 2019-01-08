package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 策略库-运营场景；positionBO运营位对象属性
 */
public class PolicySCenePositionBO {
    private String channelId="";

    private String positionId="";

    private String positionName="";

    private String scheStartTime="";

    private String scheEndTime="";

    private String positionAttrMap="";


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPositionAttrMap() {
        return positionAttrMap;
    }

    public void setPositionAttrMap(String positionAttrMap) {
        this.positionAttrMap = positionAttrMap;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getScheStartTime() {
        return scheStartTime;
    }

    public void setScheStartTime(String scheStartTime) {
        this.scheStartTime = scheStartTime;
    }

    public String getScheEndTime() {
        return scheEndTime;
    }

    public void setScheEndTime(String scheEndTime) {
        this.scheEndTime = scheEndTime;
    }
}
