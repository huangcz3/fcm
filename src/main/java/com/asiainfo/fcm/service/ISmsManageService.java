package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.ActivityOpRecordInfo;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.SmsOpEnum;

import java.util.List;

/**
 * Created by chengzhongtao on 2017/12/15.
 */
public interface ISmsManageService {

    List<ActivityOpRecordInfo> getOperateList(String activityId);

    void saveOperateInfo(String activityId, User user, int opType, SmsOpEnum parse, String reason, int rel);
}
