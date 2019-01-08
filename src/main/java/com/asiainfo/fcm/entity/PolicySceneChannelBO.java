package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 运营场景-策略库；channelBO渠道对象
 */
public class PolicySceneChannelBO {
    private String campaignId="";

    private String channelId="";

    private String channelName="";

    private String channeType="";

    private String marketInfo="";

    private String chanelTrigGerulExpl="";

    private String channelAttrMap="";

    /**
     * 运营位信息
     */
    private List<PolicySCenePositionBO> positionBOList;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getChannelAttrMap() {
        return channelAttrMap;
    }

    public void setChannelAttrMap(String channelAttrMap) {
        this.channelAttrMap = channelAttrMap;
    }

    public List<PolicySCenePositionBO> getPositionBOList() {
        return positionBOList;
    }

    public void setPositionBOList(List<PolicySCenePositionBO> positionBOList) {
        this.positionBOList = positionBOList;
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

    public String getChanneType() {
        return channeType;
    }

    public void setChanneType(String channeType) {
        this.channeType = channeType;
    }

    public String getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(String marketInfo) {
        this.marketInfo = marketInfo;
    }

    public String getChanelTrigGerulExpl() {
        return chanelTrigGerulExpl;
    }

    public void setChanelTrigGerulExpl(String chanelTrigGerulExpl) {
        this.chanelTrigGerulExpl = chanelTrigGerulExpl;
    }

}
