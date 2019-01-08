package com.asiainfo.fcm.enums;

/**
 * 活动操作枚举
 * Created by PuMg on 2017/12/13/0013.
 */
public enum ActivityOpEnum {
        CREATE("A001", "创建活动"),
        PAUSE("A002", "暂停活动"),
        RECOVERY("A003", "恢复活动"),
        DELETE("A004", "删除活动"),
        APP_PASSED("A005", "活动审批通过"),
        APP_REFUSED("A006", "活动审批驳回");

        private String code;

        private String msg;

        ActivityOpEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
}