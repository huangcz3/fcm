package com.asiainfo.fcm.entity;

/**
 * 用户信息.
 * Created by RUOK on 2017/6/26.
 */
public class User {

    /**
     * 用户id.
     */
    private String userId;

    /**
     * 用户姓名.
     */
    private String userName;

    /**
     * 用户手机号.
     */
    private String phoneNo;

    /**
     * 用户归属地市id.
     */
    private String cityId;

    /**
     * 用户归属地市名称.
     */
    private String cityName;

    /**
     * 用户密码.
     */
    private String password;

    private String userRole;

    //标记是否是从经分登录的
    private String isFirstJump;

    //保存经分登录过来的PN值
    private  String PN;

    /**
     * 所属部门
     */
    private String deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    private String city_code;
    private String[] codeArra = {"000","999","028 ","0816","0813","0812","0839","0818","0830","0826","0827","0825","0831","0832","0282","0833","0835","0838","0817","0283","0837","0836","0834"};

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId.trim();
        this.setCity_code(cityId);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getIsFirstJump() {
        return isFirstJump;
    }

    public void setIsFirstJump(String isFirstJump) {
        this.isFirstJump = isFirstJump;
    }

    public String getPN() {
        return PN;
    }

    public void setPN(String PN) {
        this.PN = PN;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code=="87" ? codeArra[2] : codeArra[Integer.parseInt(city_code)];
    }
}
