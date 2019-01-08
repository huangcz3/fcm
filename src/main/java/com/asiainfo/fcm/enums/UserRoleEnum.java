package com.asiainfo.fcm.enums;

public enum UserRoleEnum {
    ADMIN("admin"),//管理员
    MIGU_APPROVER("miguApprover"),//数字内容审批
    _10086_CHANNEL("10086channel");//触点短信渠道小类管理

    private String role;

    UserRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
