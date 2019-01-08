package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public enum TimeRuleEnum {
    DAY("D","日"),
    WEEK("W","周"),
    MONTH("M","月");

    private String code;
    private String name;

    TimeRuleEnum(String code, String name){
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
        for (TimeRuleEnum timeRuleEnum: TimeRuleEnum.values()) {
            if (timeRuleEnum.getCode().equals(code)) {
                result = timeRuleEnum.getName();
                break;
            }
        }
        return result;
    }

}
