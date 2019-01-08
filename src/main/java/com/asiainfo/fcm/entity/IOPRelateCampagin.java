package com.asiainfo.fcm.entity;

/**
 * Created by Administrator on 2017/9/22.
 */
public class IOPRelateCampagin {
    //子活动Id
    private String campaignId;
    //本地创建活动Id
    private String campId;
    //本地创建活动名称
    private String campName;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }
}
