package com.asiainfo.fcm.enums;

/**
 * Created by chengzhongtao on 2017/12/15.
 */
public enum SmsOpEnum {
    JUMP("S001","活动提前"),
    PARSE("S002","暂停活动"),
    RECOVERY("S003","恢复活动"),
    CHANGE("S004","修改速率"),
    CHANGE_FILTER("S005","修改过滤策略");

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    SmsOpEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
