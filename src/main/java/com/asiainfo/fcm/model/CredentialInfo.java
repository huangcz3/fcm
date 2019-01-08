package com.asiainfo.fcm.model;

public class CredentialInfo {
    private String userId;
    private boolean hasAuditRight;
    private String ssoTime;
    private int cityId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isHasAuditRight() {
        return hasAuditRight;
    }

    public void setHasAuditRight(boolean hasAuditRight) {
        this.hasAuditRight = hasAuditRight;
    }

    public String getSsoTime() {
        return ssoTime;
    }

    public void setSsoTime(String ssoTime) {
        this.ssoTime = ssoTime;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
