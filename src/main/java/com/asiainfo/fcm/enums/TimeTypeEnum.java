package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public enum  TimeTypeEnum {
    NO_EVENT("0","无事件"),
    INTERNET_USE_EVENT("1","互联网使用事件"),
    SOCIAL_EVENT("2","社会事件"),
    POSITION_TRACK_EVENT("3","位置行踪事件"),
    BUSSINESS_HANDLING_EVENT("4","业务办理事件"),
    BUSSINESS_USEING_EVENT("5","业务使用事件"),
    CYCLICAL_BUSSINESS_EVENT("6","周期业务事件"),
    SELF_SERVICE_SYSTEM_CONTACT_EVENT("7","自助系统接触事件"),
    PCC_EVENT("8","PCC事件"),
    OTHER_EVENT("9","其它事件");

    private String code;
    private String name;

    TimeTypeEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    public String getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public  static String getName(String code){
        String result = "";
        for (TimeTypeEnum  timeTypeEnum: TimeTypeEnum.values()) {
            if (timeTypeEnum.getCode().equals(code)) {
                result = timeTypeEnum.getName();
                break;
            }
        }
        return result;
    }
}
