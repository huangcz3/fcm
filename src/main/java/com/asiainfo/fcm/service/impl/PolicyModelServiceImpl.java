package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ActivityTypeEnum;
import com.asiainfo.fcm.mapper.primary.PolicyModelMapper;
import com.asiainfo.fcm.mapper.primary.PolicySceneMapper;
import com.asiainfo.fcm.service.IPolicyModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
@Service
public class PolicyModelServiceImpl implements IPolicyModelService {
    @Autowired
    private PolicyModelMapper policyModelMapper;

    @Autowired
    private PolicySceneMapper policySceneMapper;

    @Override
    public List<PolicyModelActivity> getPolicyModelList(Map paramMap){
        String createId = paramMap.get("userId").toString();
        String type = paramMap.get("type").toString();
        List<PolicyModelActivity> activityList = policyModelMapper.getPolicyModelList(paramMap);

        if( type.equals("1") ){
            for(PolicyModelActivity activity : activityList){
                List<String> canUploaders = policySceneMapper.getPolicyUploaders("2");
                if (activity.getCreateId().equals(createId) || canUploaders.contains(createId)) {
                    if (!activity.getIsCanUpload().equals("0")) {
                        activity.setIsCanUpload("1");
                    }
                    activity.setIsCanDelete("1");
                }
            }
        }

        return activityList;
    }

    @Override
    public int getPolicyModelListTotals(Map paramMap){
        return policyModelMapper.getPolicyModelListTotals(paramMap);
    }

    @Override
    public void savePolicyModelActivity(PolicyModelActivity activity){
        policyModelMapper.savePolicyModelActivity(activity);
        //保存数据描述对象
        policyModelMapper.savePolicyModelDataDescBO(activity.getDataDesc());
        //数据预处理对象
        policyModelMapper.savePolicyModeDataHandleBO(activity.getDataHandle());
        //保存模型评估指标对象
        policyModelMapper.savePolicyModelModeEvaBO(activity.getModeEva());
        //保存业务效果评估对象
        policyModelMapper.savePolicyModelKpiBO(activity.getModelKpiBO());
    }

    @Override
    public PolicyModelActivity getPolicyModelDetail(String modelId){
        PolicyModelActivity activityBean = policyModelMapper.getPolicyModelDetail(modelId);
        //查询数据描述对象
        activityBean.setDataDesc(policyModelMapper.getPolicyModelDataDescBO(modelId));
        //查询数据预处理对象
        activityBean.setDataHandle(policyModelMapper.getPolicyModelDataHandleBO(modelId));
        //查询模型评估指标对象
        activityBean.setModeEva(policyModelMapper.getPolicyModelModeEvaBO(modelId));
        //查询业务效果评估对象
        activityBean.setModelKpiBO(policyModelMapper.getPolicyModelKpiBO(modelId));
        return  activityBean;
    }

    @Override
    public int isCanUp(Map paramMap){
        return policyModelMapper.isCanUp(paramMap);
    }

    @Override
    public void uploadActivity(Map paramMap){
        policyModelMapper.uploadActivity(paramMap);
    }

    @Override
    public void deleteActivity(Map paramMap){
        policyModelMapper.deleteActivity(paramMap);
    }

    @Override
    public  List<Map<String,String>> getAllPolicyModelList(Map paramMap){
        String type = paramMap.get("type").toString();
        List<Map<String,String>> allList = policyModelMapper.getAllPolicyModelList(paramMap);

        for (Map row : allList){
            String modelId = row.get("MODELID").toString();
            String modelType = row.get("MODELTYPE").toString();
            String createTime = row.get("CREATETIME").toString();
            String updateTime = row.get("UPDATETIME").toString();

            if( row.get("UPLOADDATETIME")!= null ){
                String uploadDateTime = row.get("UPLOADDATETIME").toString();
                row.put("UPLOADDATETIME",this.convertTimeToDateWithConnector(uploadDateTime));
            }
            row.put("CREATETIME",this.convertTimeToDateWithConnector(createTime));
            row.put("UPDATETIME",this.convertTimeToDateWithConnector(updateTime));
            if ( modelType != "" && modelType != null ){
                row.put("MODELTYPE", ActivityTypeEnum.getName(modelType));
            }

            if( type.equals("1") ){
                String flag = row.get("FLAG").toString();
                if( flag.equals("1") ){
                    row.put("FLAG","保存待上传");
                }else if(flag.equals("2") || flag.equals("10")){
                    row.put("FLAG","上传中");
                }else if(flag.equals("4")){
                    row.put("FLAG","上传成功");
                }else{
                    row.put("FLAG","上传失败");
                }
            }else{
                row.put("FLAG","集团审批通过并发布");
            }


            PolicyModelDataDescBO dataDescBO = policyModelMapper.getPolicyModelDataDescBO(modelId);
            row.putAll(this.convertBean2Map(dataDescBO));
            PolicyModelDataHandleBO handleBO = policyModelMapper.getPolicyModelDataHandleBO(modelId);
            row.putAll(this.convertBean2Map(handleBO));
            PolicyModelModeEvaBO modeEvaBO = policyModelMapper.getPolicyModelModeEvaBO(modelId);
            row.putAll(this.convertBean2Map(modeEvaBO));
            PolicyModelKpiBO kpiBO = policyModelMapper.getPolicyModelKpiBO(modelId);
            row.putAll(this.convertBean2Map(kpiBO));
        }
        return allList;
    }

    private Map convertBean2Map(Object javaBean) {
        Map map = new HashMap();

        try {
            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {
                String propertyName = null; // javaBean属性名
                Object propertyValue = null; // javaBean属性值
                for (PropertyDescriptor pd : propertyDescriptors) {
                    propertyName = pd.getName().toUpperCase();
                    if (!propertyName.equals("class")) {
                        Method readMethod = pd.getReadMethod();
                        propertyValue = readMethod.invoke(javaBean, new Object[0]);
                        map.put(propertyName, propertyValue);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
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
