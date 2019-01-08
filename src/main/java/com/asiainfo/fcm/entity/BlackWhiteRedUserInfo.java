package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/8/7/0007.
 * 黑白红名单用户信息
 */
public class BlackWhiteRedUserInfo {

    private String phoneNo;

    private String userType;

    //用户来源 0后台更新 1前台添加
    private int userSource;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserSource() {
        return userSource;
    }

    public void setUserSource(int userSource) {
        this.userSource = userSource;
    }
}
