package com.asiainfo.fcm.service;

import java.util.Map;

/**
 * Created by PuMg on 2017/8/18/0018.
 */
public interface IPccService {
    //查询活动策略信息
    Map getActivityPolicyInfo(String activityId);

    //记录ftp 发送日志
    void updateFtpState(Map map);
}
