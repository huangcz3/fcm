package com.asiainfo.fcm.service;

import com.asiainfo.fcm.model.OnlineBehaviorInfo;

import java.util.List;

/**
 * Created by PuMg on 2017/8/14/0014.
 */
public interface IOnlineBehaviorTaskService {

    //获取活动信息（上网行为）
    List<OnlineBehaviorInfo> getOnlineBehaviorInfo();

    //是否已保存
    int isExsitBehavioInfo(String activityId);

    //保存活动上网行为信息
    void saveBehaviorInfo();

}
