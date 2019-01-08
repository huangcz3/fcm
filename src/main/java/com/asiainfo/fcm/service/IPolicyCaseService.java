package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.PolicyCaseActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/12.
 */
public interface IPolicyCaseService {
    void saveNewPolicyCase(PolicyCaseActivity caseActivity);
    List<PolicyCaseActivity> getPolicyCaseList(Map parameterMap);
    int getPolicyCaseListTotals(Map parameterMap);
    int isCanUp(Map parameterMap);
    void uploadActivity(Map parameterMap);
    void deletePolicyCase(Map parameterMap);
    PolicyCaseActivity getPolicyCaseDetail(String caseId);
    List<Map<String ,String>> getAllPolicyCaseList(Map parameterMap);
}
