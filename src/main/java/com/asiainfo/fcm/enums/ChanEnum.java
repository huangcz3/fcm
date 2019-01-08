package com.asiainfo.fcm.enums;

/**
 * Created by chengzhongtao on 2017/12/25.
 */
public enum ChanEnum {

    ALL("0","10086#10658211"),
    _10086("1","10086"),
    SALES("2","10658211");

    private String code;
    private String msg;

    ChanEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
