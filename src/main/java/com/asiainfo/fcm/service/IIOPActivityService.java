package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */
public interface IIOPActivityService {
    /**
     * 查询集团活动派单列表
     */
    List<IOPActivityInfo> getActivityList(Map parameterMap);

    int getActivityListTotals(Map parameterMap);

    String getUserAuthority(String userPhoneNo);

    /**
     * 查询主活动下所有子活动的共有属性 -- 效果评估 和 PCC策略
     */
    IOPActivityInfo getPublicAttrInfo(String activityId);

    /**
     * 查询主活动下子活动
     */
    List<IOPCampaginInfo> getCampaginInfo(String activityId);

    Map getBelongerInfo(Map parameterMap);

    /**
     * 查询对应派单用户列表
     */
    List<IOPUserInfo> getAllUserList();

    /**
     * 派单管理，将某活动派单下发某用户
     */
    IopSendOrder checkSendOrderInfo(IopSendOrder parameterMap);

    void insertIntoG2PActDownRel(IopSendOrder parameterMap);

    void insertIntoCampaginAndCampRel(List<IOPRelateCampagin> parameterList);

    List<IOPRelateCampagin> getRelateCampaginAndCamp(String campaignId);
}
