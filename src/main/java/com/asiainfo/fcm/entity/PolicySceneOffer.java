package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 策略库--运营场景；offerBO产品对象属性
 */
public class PolicySceneOffer {
    private String campaignId="";

    private String offerCode="";

    private String offerName="";

    private String offerDesc="";

    private String offerType="";

    private String offerSource="";

    private String offerImg="";

    private String offerUrl="";

    private String feeModel="";

    private String marketInfo="";

    /**
     * 产品可扩展信息
     */
    private String offerAttrMap;

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getOfferCode() {
        return offerCode;
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferSource() {
        return offerSource;
    }

    public void setOfferSource(String offerSource) {
        this.offerSource = offerSource;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }

    public String getOfferUrl() {
        return offerUrl;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public String getFeeModel() {
        return feeModel;
    }

    public void setFeeModel(String feeModel) {
        this.feeModel = feeModel;
    }

    public String getMarketInfo() {
        return marketInfo;
    }

    public void setMarketInfo(String marketInfo) {
        this.marketInfo = marketInfo;
    }

    public String getOfferAttrMap() {
        return offerAttrMap;
    }

    public void setOfferAttrMap(String offerAttrMap) {
        this.offerAttrMap = offerAttrMap;
    }
}
