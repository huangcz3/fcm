package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */
public interface IOPActivityMapper {
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

    IopSendOrder checkSendOrderInfo(IopSendOrder parameterMap);
    void deleteCampsByCampaignId(String campaignId);
    void insertIntoG2PActDownRel(IopSendOrder parameterMap);

    IOPRelateCampagin checkRepeatInfo(IOPRelateCampagin parameterMap);
    void insertIntoCampaginAndCampRel(IOPRelateCampagin parameterMap);

    List<IOPRelateCampagin> getRelateCampaginAndCamp(String campaignId);
}
