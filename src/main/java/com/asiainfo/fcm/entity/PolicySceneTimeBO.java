package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 策略库-运营场景，timeBO时机策略对象属性
 */
public class PolicySceneTimeBO {
    private String campaignId="";

    private String timeId="";

    private String timeDistindes="";

    private String timeRule="";

    private String channelRule="";

    private String muchrunOrder="";

    private String timeAttrMap="";


    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getTimeDistindes() {
        return timeDistindes;
    }

    public void setTimeDistindes(String timeDistindes) {
        this.timeDistindes = timeDistindes;
    }

    public String getTimeRule() {
        return timeRule;
    }

    public void setTimeRule(String timeRule) {
        this.timeRule = timeRule;
    }

    public String getChannelRule() {
        return channelRule;
    }

    public void setChannelRule(String channelRule) {
        this.channelRule = channelRule;
    }

    public String getMuchrunOrder() {
        return muchrunOrder;
    }

    public void setMuchrunOrder(String muchrunOrder) {
        this.muchrunOrder = muchrunOrder;
    }

    public String getTimeAttrMap() {
        return timeAttrMap;
    }

    public void setTimeAttrMap(String timeAttrMap) {
        this.timeAttrMap = timeAttrMap;
    }
}
