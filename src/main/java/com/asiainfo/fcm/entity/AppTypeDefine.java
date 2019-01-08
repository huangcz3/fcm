package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/7/25/0025.
 * 上网行为APP分类信息
 */
public class AppTypeDefine {
    //类型ID
    private String appTypeId;
    //类型名称
    private String  appTypeName;
    //类型描述
    private String  appTypeDesc;
    //是否有效 0 无效 1 有效
    private int effective;

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public String getAppTypeName() {
        return appTypeName;
    }

    public void setAppTypeName(String appTypeName) {
        this.appTypeName = appTypeName;
    }

    public String getAppTypeDesc() {
        return appTypeDesc;
    }

    public void setAppTypeDesc(String appTypeDesc) {
        this.appTypeDesc = appTypeDesc;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }
}
