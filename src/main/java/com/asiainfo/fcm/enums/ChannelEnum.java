package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public enum ChannelEnum {
    BUSSINESS_HALL("1","营业厅"),
    HANDHELD_HALL("2","掌厅"),
    INTERNET_HALL("3","网厅"),
    SELF_SERVICE_TERMINAL("4","自助终端"),
    MESSAGE("5","短信"),
    MULTIMEDIA_MESSAGE("6","彩信"),
    PHONE_APP_STORE("7","手机APP营业厅"),
    HANDHELD_BOSS("8","掌上BOSS"),
    WECHAT("9","微信"),
    MAILBOX("10","139邮箱"),
    SELF_POSSESSED_PRODUCT_AND_APP("11","自有业务产品及APP"),
    ESOP("12","ESOP"),
    CALL_OUT("13","外呼"),
    OTHER_CHANNEL("14","省内自有其它类触点");

    private String code;
    private String name;

    ChannelEnum(String code,String name){
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
        for (ChannelEnum  channelEnum: ChannelEnum.values()) {
            if (channelEnum.getCode().equals(code)) {
                result = channelEnum.getName();
                break;
            }
        }
        return result;
    }
}
