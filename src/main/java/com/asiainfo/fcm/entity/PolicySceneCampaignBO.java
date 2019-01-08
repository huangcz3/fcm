package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by ShaoJinyu on 2017/12/6.
 */
public class PolicySceneCampaignBO {
    private String activityId="";

    private String campaignId="";

    private String campaignName="";

    private String campaignStartTime="";

    private String campaignEndTime="";

    /**
     * 时机策略
     */
    private PolicySceneTimeBO timeBO;
    /**
     * 目标客户群
     */
    private PolicySceneSegmentBO segmentBO;
    /**
     * 产品
     */
    private List<PolicySceneOffer> offerList;
    /**
     * 渠道
     */
    private PolicySceneChannelBO channelBO;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignStartTime() {
        return campaignStartTime;
    }

    public void setCampaignStartTime(String campaignStartTime) {
        this.campaignStartTime = campaignStartTime;
    }

    public String getCampaignEndTime() {
        return campaignEndTime;
    }

    public void setCampaignEndTime(String campaignEndTime) {
        this.campaignEndTime = campaignEndTime;
    }

    public PolicySceneTimeBO getTimeBO() {
        return timeBO;
    }

    public void setTimeBO(PolicySceneTimeBO timeBO) {
        this.timeBO = timeBO;
    }

    public PolicySceneSegmentBO getSegmentBO() {
        return segmentBO;
    }

    public void setSegmentBO(PolicySceneSegmentBO segmentBO) {
        this.segmentBO = segmentBO;
    }

    public List<PolicySceneOffer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<PolicySceneOffer> offerList) {
        this.offerList = offerList;
    }

    public PolicySceneChannelBO getChannelBO() {
        return channelBO;
    }

    public void setChannelBO(PolicySceneChannelBO channelBO) {
        this.channelBO = channelBO;
    }
}
