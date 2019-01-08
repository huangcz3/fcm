package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 渠道类型维表
 */
public class ChannelType {
    //渠道类型ID
    private int channelTypeId;
    //渠道类型名称
    private String channelTypeName;

    public int getChannelTypeId() {
        return channelTypeId;
    }
    public void setChannelTypeId(int channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    public String getChannelTypeName() {
        return channelTypeName;
    }

    public void setChannelTypeName(String channelTypeName) {
        this.channelTypeName = channelTypeName;
    }
}
