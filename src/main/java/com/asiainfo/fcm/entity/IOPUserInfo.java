package com.asiainfo.fcm.entity;

/**
 * Created by Administrator on 2017/9/20.
 */
public class IOPUserInfo {
    private String userName;

    private String phoneNo;

    private String permitionCode;

    private String cityId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPermitionCode() {
        return permitionCode;
    }

    public void setPermitionCode(String permitionCode) {
        this.permitionCode = permitionCode;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
