package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/15.
 */
public enum ActivityObjectiveEnum {
    NEW_CUSTOMER("1","新增客户类"),
    STOCK_POSSESS("2","存量保有类"),
    VALUE_PROMITION("3","价值提升类"),
    OFF_NEWWORK_WARNING("4","离网预警"),
    OTHER_CLASS("9","其他类");


    private String code;
    private String name;
    ActivityObjectiveEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public  static String getName(String code){
        String result = "";
        for (ActivityObjectiveEnum  activityObjectiveEnum: ActivityObjectiveEnum.values()) {
                if (activityObjectiveEnum.getCode().equals(code)) {
                result = activityObjectiveEnum.getName();
                break;
            }
        }
        return result;
    }
}
