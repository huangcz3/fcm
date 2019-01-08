package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/6.
 */
public interface PolicySceneMapper {
    void savePolicySceneBasicInfo(PolicySceneActivity activity);
    void savePolicySceneKpiBO(PolicySceneKpiBO policySceneKpiBO);
    void savePolicySceneCamBasicInfo(PolicySceneCampaignBO campaignBO);
    void savePolicySceneCamChannelBO(PolicySceneChannelBO channelBO);
    void savePolicySceneCamsegmentBO(PolicySceneSegmentBO sceneSegmentBO);
    void savePolicySceneTimeBO(PolicySceneTimeBO timeBO);
    void savePolicySceneOfferBO(PolicySceneOffer offerBO);
    List<PolicySceneActivity> getPolicySceneList(Map parameterMap);
    int getPolicySceneListTotals(Map parameterMap);
    List<String> getPolicyUploaders(String type);
    int isCanUp(Map paramMap);
    void uploadActivity(Map parameterMap);
    PolicySceneActivity getActivityDetail(String activityId);
    PolicySceneKpiBO getPolicySceneKpiBO(String activityId);
    List<PolicySceneCampaignBO> getPolicySceneCampaignBO(String activityId);
    PolicySceneChannelBO getPolicySceneCamChannelBO(String campaignId);
    PolicySceneSegmentBO getPolicySceneCamsegmentBO(String campaignId);
    PolicySceneTimeBO getPolicySceneCamTimeBO(String campaignId);
    List<PolicySceneOffer> getPolicySceneCamOffer(String campaignId);
    List<Map<String,String>> getAllPolicySceneList(Map parameterMap);
    void deleteSceneActivity(Map parameterMap);
    Map<String,Object> getActivityEvalutionIndexs(String activityId);
}
