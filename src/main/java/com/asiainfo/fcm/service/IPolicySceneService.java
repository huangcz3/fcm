package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.PolicySceneActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/6.
 */
public interface IPolicySceneService {
    void savePolicySceneActivity(PolicySceneActivity activity);
    List<PolicySceneActivity> getPolicySceneList(Map parameterMap);
    int getPolicySceneListTotals(Map parameterMap);
    void uploadActivity(Map parameterMap);
    int isCanUp(Map paramMap);
    PolicySceneActivity getSceneActivityDetail(String activityId);
    List<Map<String,String >> getAllPolicySceneList(Map parameterMap);
    void deleteSceneActivity(Map parameterMap);
    Map<String,Object> getActivityEvalutionIndexs(String activityId);
}
