package com.asiainfo.fcm.entity;

/**
 * 业务类型.
 * Created by RUOK on 2017/6/15.
 */
public class BusinessType {

    /**
     * 业务类型id.
     */
    private String businessTypeId;

    /**
     * 业务类型名称.
     */
    private String businessTypeName;

    /**
     * 是否有效：0无效，1有效.
     */
    private String effective;

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }
}
