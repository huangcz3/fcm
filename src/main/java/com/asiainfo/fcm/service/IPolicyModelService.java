package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.PolicyModelActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public interface IPolicyModelService {
    List<PolicyModelActivity> getPolicyModelList(Map paramMap);
    int getPolicyModelListTotals(Map paramMap);
    void savePolicyModelActivity(PolicyModelActivity activity);
    PolicyModelActivity getPolicyModelDetail(String modelId);
    int isCanUp(Map paramMap);
    void uploadActivity(Map paramMap);
    void deleteActivity(Map paramMap);
    List<Map<String,String>> getAllPolicyModelList(Map paramMap);
}
