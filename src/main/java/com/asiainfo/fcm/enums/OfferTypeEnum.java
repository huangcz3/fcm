package com.asiainfo.fcm.enums;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public enum OfferTypeEnum {
    TELECOM_SERVICE("1","电信服务"),
    CUSTOM_SERVICE("2","客户服务"),
    DIGITAL_CONTENT_SERVICE("3","数字内容服务"),
    MATERIAL_OBJECT("4","实物"),
    VIRTURAL_OBJECT("5","虚拟物品");

    private String code;
    private String name;

    OfferTypeEnum(String code,String name){
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
        for (OfferTypeEnum  offerTypeEnum: OfferTypeEnum.values()) {
            if (offerTypeEnum.getCode().equals(code)) {
                result = offerTypeEnum.getName();
                break;
            }
        }
        return result;
    }
}
