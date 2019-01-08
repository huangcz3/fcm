package com.asiainfo.fcm.mapper.primary;

import java.util.Map;

/**
 * Created by RUOK on 2017/6/29.
 */
public interface PccMapper {

    Map getActivityPolicyInfo(String activityId);

    void updateFtpState(Map map);
}

