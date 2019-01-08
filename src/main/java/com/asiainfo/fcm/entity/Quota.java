package com.asiainfo.fcm.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/28.
 */
public class Quota {
    private String cityId;

    private String cityName;

    private String channelId;

    private String channelName;

    private int quotaType;

    private int sendLimit;

    private int sendedCount;

    private String effectTime;

    private int effective;

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(int quotaType) {
        this.quotaType = quotaType;
    }

    public int getSendLimit() {
        return sendLimit;
    }

    public void setSendLimit(int sendLimit) {
        this.sendLimit = sendLimit;
    }

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }

    public int getSendedCount() {
        return sendedCount;
    }

    public void setSendedCount(int sendedCount) {
        this.sendedCount = sendedCount;
    }
}
