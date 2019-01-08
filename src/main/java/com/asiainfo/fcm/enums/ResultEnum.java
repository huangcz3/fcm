package com.asiainfo.fcm.enums;

/**
 * 返回结果枚举.
 * Created by RUOK on 2017/6/15.
 */
public enum ResultEnum {
    /**
     *
     */
    SUCCESS(0, "成功"),
    NOT_LOGIN(1, "未登录或登录超时"),
    PERMISSION_DENIED(2, "无相关权限"),
    MAX_UPLOAD_SIZE_EXCEEDED(3, "上传文件大小超过限制"),
    UNKNOWN_FILE_TYPE(4, "未知文件类型"),
    CUSTOMER_GROUP_FILE_TYPE_NOT_MATCH(5, "客户群文件类型不匹配"),
    IMAGE_FILE_TYPE_NOT_MATCH(6, "图片文件类型不匹配"),
    ILLEGAL_FILE_USE_TYPE(7, "未指定的上传用途"),
    FILE_IS_BLANK(8, "空白文件"),
    NO_VALID_DATA(9, "无有效数据"),
    MAX_RECORDS_EXCEEDED(10, "目标用户数超过最大限制"),
    FILE_TYPE_NOT_MATCH(11, "文件类型不匹配"),
    MPP_TABLE_NOT_EXIST(12, "表不存在！"),
    MPP_TABLE_PHONE_COLUMN_NOT_EXIST(12, "表中手机号码字段不存在！"),
    MPP_TABLE_MARKETING_COLUMN_NOT_EXIST(12, "表中个性化营销语字段不存在！"),
    UNKNOWN_ERROR(-1, "未知错误"),
    ISEXSIT_ERROR(-2, "号码已存在"),
    HAS_SENDER_ORDER_ERROR(-3, "该活动已经指派"),
    ISEXSIT_ACT_ERROR(-4, "该活动已存在"),
    NOT_EFFECTIVE_URL(-5, "链接无效，请重新获取！"),
    IS_REPEAT_LOGIN(-6, "已登录！"),
    NO_CITY_NEED_QUOTA(-7, "无效操作！该渠道下所有地市均已设置配额"),
    INFO_EXIST(-8, "信息已存在"),
    ACTIVITY_ID_DOES_NOT_EXIST(-9, "活动编码不存在，该附件无法上传"),
    ACTIVITY_HAS_FINISHED(-10, "该活动已执行结束"),
    QUOTA_EXIST(-11, "该地市该月渠道配额已存在");

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
