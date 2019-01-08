package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/7/4/0004.
 */
public class ActivityRecommendProduct {
    //活动id
    private String activityId;
    //活动名称
    private String activityName;
    //推荐类型：1资费，2营销活动，3宣传
    private int productType;
    //推荐产品id
    private String productId;
    //推荐产品名称
    private String productName;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
