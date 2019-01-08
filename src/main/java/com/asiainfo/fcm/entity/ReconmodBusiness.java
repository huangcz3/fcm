package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/7/3/0003.
 */
public class ReconmodBusiness {

    private String activityId;
    /**
     * 推荐业务类型id.
     */
    private String businessId;
    /**
     * 推荐业务类型
     */
    private int businessType;
    /**
     * 推荐业务类型名称.
     */
    private String businessName;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
