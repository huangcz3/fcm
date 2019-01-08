package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public interface PolicyModelMapper {
    List<PolicyModelActivity> getPolicyModelList(Map paramMap);
    int getPolicyModelListTotals(Map paramMap);
    void savePolicyModelActivity(PolicyModelActivity activity);
    void savePolicyModelDataDescBO(PolicyModelDataDescBO dataDesc);
    void savePolicyModeDataHandleBO(PolicyModelDataHandleBO dataHandle);
    void savePolicyModelModeEvaBO(PolicyModelModeEvaBO modeEva);
    void savePolicyModelKpiBO(PolicyModelKpiBO kpiBO);
    PolicyModelActivity getPolicyModelDetail(String modelId);
    PolicyModelDataDescBO getPolicyModelDataDescBO(String modelId);
    PolicyModelDataHandleBO getPolicyModelDataHandleBO(String modelId);
    PolicyModelModeEvaBO getPolicyModelModeEvaBO(String modelId);
    PolicyModelKpiBO getPolicyModelKpiBO(String modelId);
    int isCanUp(Map paramMap);
    void uploadActivity(Map paramMap);
    void deleteActivity(Map paramMap);
    List<Map<String,String>> getAllPolicyModelList(Map paramMap);
}
