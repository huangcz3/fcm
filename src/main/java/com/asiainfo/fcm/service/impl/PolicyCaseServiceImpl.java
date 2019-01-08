package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.PolicyCaseActivity;
import com.asiainfo.fcm.mapper.primary.PolicyCaseMapper;
import com.asiainfo.fcm.mapper.primary.PolicySceneMapper;
import com.asiainfo.fcm.service.IPolicyCaseService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/12.
 */
@Service
public class PolicyCaseServiceImpl implements IPolicyCaseService {
    @Autowired
    private PolicyCaseMapper policyCaseMapper;

    @Autowired
    private PolicySceneMapper policySceneMapper;

    @Override
    public void saveNewPolicyCase(PolicyCaseActivity caseActivity){
        policyCaseMapper.saveNewPolicyCase(caseActivity);
    }

    @Override
    public List<PolicyCaseActivity> getPolicyCaseList(Map parameterMap){
        String createId = parameterMap.get("userId").toString();
        String caseType = parameterMap.get("caseType").toString();
        List<PolicyCaseActivity> caseActivityList = policyCaseMapper.getPolicyCaseList(parameterMap);

        if (caseType.equals("1")){
            for(PolicyCaseActivity activity : caseActivityList){
                List<String> canUploaders = policySceneMapper.getPolicyUploaders("3");
                if (activity.getCreateId().equals(createId) || canUploaders.contains(createId)) {
                    if (!activity.getIsCanUpload().equals("0")) {
                        activity.setIsCanUpload("1");
                    }
                    activity.setIsCanDelete("1");
                }
            }
        }

        return caseActivityList;
    }

    @Override
    public int getPolicyCaseListTotals(Map parameterMap){
        return policyCaseMapper.getPolicyCaseListTotals(parameterMap);
    }

    @Override
    public int isCanUp(Map parameterMap){
        return policyCaseMapper.isCanUp(parameterMap);
    }

    @Override
    public void uploadActivity(Map parameterMap){
        policyCaseMapper.uploadActivity(parameterMap);
    }

    @Override
    public void deletePolicyCase(Map parameterMap){
        policyCaseMapper.deletePolicyCase(parameterMap);
    }

    @Override
    public PolicyCaseActivity getPolicyCaseDetail(String caseId){
        return policyCaseMapper.getPolicyCaseDetail(caseId);
    }

    @Override
    public List<Map<String ,String>> getAllPolicyCaseList(Map parameterMap){
        String type = parameterMap.get("caseType").toString();
        List<Map<String ,String>> policyCaseActivityList = policyCaseMapper.getAllPolicyCaseList(parameterMap);
        for (Map policyCaseActivity: policyCaseActivityList){
            if( policyCaseActivity.get("CASEATTRMAP") != null ){
                JSONObject extrMap = JSONObject.fromObject(policyCaseActivity.get("CASEATTRMAP").toString());
                policyCaseActivity.put("ATTACHMENTNAME",extrMap.get("attachmentName").toString());
            }
            if( type.equals("1") ){
                if( policyCaseActivity.get("FLAG") != null ){
                    String flag = policyCaseActivity.get("FLAG").toString();
                    if( flag.equals("1") ){
                        policyCaseActivity.put("FLAG","保存待上传");
                    }else if(flag.equals("2") || flag.equals("10")){
                        policyCaseActivity.put("FLAG","上传中");
                    }else if(flag.equals("4")){
                        policyCaseActivity.put("FLAG","上传成功");
                    }else{
                        policyCaseActivity.put("FLAG","上传失败");
                    }
                }
            }else{
                policyCaseActivity.put("FLAG","集团审核通过并发布");
            }

            if( policyCaseActivity.get("UPDATETIME") != null ){
                String upDateTime = policyCaseActivity.get("UPDATETIME").toString();
                policyCaseActivity.put("UPDATETIME",this.convertTimeToDateWithConnector(upDateTime));
            }
        }
        return policyCaseActivityList;
    }

    private String convertTimeToDateWithConnector(String dateStr) {
        String newDateStr = dateStr;
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter1.setLenient(false);
            Date date = formatter1.parse(dateStr);
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            newDateStr =  formatter2.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return newDateStr;
    }
}
