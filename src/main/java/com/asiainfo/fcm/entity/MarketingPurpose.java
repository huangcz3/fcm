package com.asiainfo.fcm.entity;

/**
 * 营销目的.
 * Created by RUOK on 2017/6/15.
 */
public class MarketingPurpose {

    /**
     * 营销目的id.
     */
    private String marketingPurposeId;

    /**
     * 营销目的名称.
     */
    private String marketingPurposeName;

    /**
     * 是否有效：0无效，1有效.
     */
    private String effective;

    public String getMarketingPurposeId() {
        return marketingPurposeId;
    }

    public void setMarketingPurposeId(String marketingPurposeId) {
        this.marketingPurposeId = marketingPurposeId;
    }

    public String getMarketingPurposeName() {
        return marketingPurposeName;
    }

    public void setMarketingPurposeName(String marketingPurposeName) {
        this.marketingPurposeName = marketingPurposeName;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }
}
