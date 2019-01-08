package com.asiainfo.fcm.entity;

/**
 * 客户群明细.
 * Created by RUOK on 2017/6/29.
 */
public class CustomerGroupDetail {

    /**
     * 客户手机号.
     */
    private String phoneNo;

    /**
     * 定制营销语.
     */
    private String marketing;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMarketing() {
        return marketing;
    }

    public void setMarketing(String marketing) {
        this.marketing = marketing;
    }
}
