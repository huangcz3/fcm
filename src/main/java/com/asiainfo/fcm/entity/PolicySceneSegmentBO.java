package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/6.
 * 策略库-运营场景；segmentBO目标客户群对象属性
 */
public class PolicySceneSegmentBO {
    private String campaignId="";

    private String sgmtId="";

    private String sgmtName="";

    private String sgmtNum="";

    private String sgmtSiftRule="";

    private String sgmtDesc="";

    private String dwnclietBigFine="";

    /**
     * 客户明细附件文件名称
     */
    private String sgmtAttrUrlName;
    /**
     * 客户明细附件所在FTP的地址URL
     */
    private String sgmtAttrUrlURL;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getSgmtId() {
        return sgmtId;
    }

    public void setSgmtId(String sgmtId) {
        this.sgmtId = sgmtId;
    }

    public String getSgmtName() {
        return sgmtName;
    }

    public void setSgmtName(String sgmtName) {
        this.sgmtName = sgmtName;
    }

    public String getSgmtNum() {
        return sgmtNum;
    }

    public void setSgmtNum(String sgmtNum) {
        this.sgmtNum = sgmtNum;
    }

    public String getSgmtSiftRule() {
        return sgmtSiftRule;
    }

    public void setSgmtSiftRule(String sgmtSiftRule) {
        this.sgmtSiftRule = sgmtSiftRule;
    }

    public String getSgmtDesc() {
        return sgmtDesc;
    }

    public void setSgmtDesc(String sgmtDesc) {
        this.sgmtDesc = sgmtDesc;
    }

    public String getDwnclietBigFine() {
        return dwnclietBigFine;
    }

    public void setDwnclietBigFine(String dwnclietBigFine) {
        this.dwnclietBigFine = dwnclietBigFine;
    }

    public String getSgmtAttrUrlName() {
        return sgmtAttrUrlName;
    }

    public void setSgmtAttrUrlName(String sgmtAttrUrlName) {
        this.sgmtAttrUrlName = sgmtAttrUrlName;
    }

    public String getSgmtAttrUrlURL() {
        return sgmtAttrUrlURL;
    }

    public void setSgmtAttrUrlURL(String sgmtAttrUrlURL) {
        this.sgmtAttrUrlURL = sgmtAttrUrlURL;
    }
}
