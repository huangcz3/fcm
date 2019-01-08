package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public enum ActivityTypeEnum {
    FOUR_GBYTE__PRODUCT("1","4G产品类"),
    TERMIAL("2","终端类"),
    FLUX("3","流量类"),
    DIGITAL_SERVICE("4","数字化服务类"),
    BASIC_SERVICE("5","基础服务类"),
    PCC("6","PCC类"),
    BROADBAND("7","宽带类"),
    OTHER("9","其它类");

    private String code;
    private String name;

    ActivityTypeEnum(String code,String name){
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
        for (ActivityTypeEnum  activityTypeEnum: ActivityTypeEnum.values()) {
            if (activityTypeEnum.getCode().equals(code)) {
                result = activityTypeEnum.getName();
                break;
            }
        }
        return result;
    }
}
