package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.*;
import com.asiainfo.fcm.mapper.primary.PolicySceneMapper;
import com.asiainfo.fcm.service.IPolicySceneService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShaoJinyu on 2017/12/6.
 */
@Service
public class PolicySceneServiceImpl implements IPolicySceneService {
    @Autowired
    private PolicySceneMapper policySceneMapper;

    @Override
    public void savePolicySceneActivity(PolicySceneActivity activity){
        //保存活动基础信息
        policySceneMapper.savePolicySceneBasicInfo(activity);
        //保存kpiBO活动效果评估指标
        policySceneMapper.savePolicySceneKpiBO(activity.getKpiBO());
        //保存campaignList子活动信息
        List<PolicySceneCampaignBO> campaignList = activity.getCampaignList();
        for (PolicySceneCampaignBO campaignBO: campaignList){
            //保存子活动基础信息
            policySceneMapper.savePolicySceneCamBasicInfo(campaignBO);
            //保存channelBO渠道信息
            policySceneMapper.savePolicySceneCamChannelBO(campaignBO.getChannelBO());
            //保存segmentBO目标客户群
            policySceneMapper.savePolicySceneCamsegmentBO(campaignBO.getSegmentBO());
            //保存timeBO时机策略
            policySceneMapper.savePolicySceneTimeBO(campaignBO.getTimeBO());
            //保存List<OfferBO>产品
            List<PolicySceneOffer> offerList = campaignBO.getOfferList();
            for( PolicySceneOffer offerBO : offerList ){
                policySceneMapper.savePolicySceneOfferBO(offerBO);
            }
        }
    }

    @Override
    public List<PolicySceneActivity> getPolicySceneList(Map paramMap){
        String createId = paramMap.get("createId").toString();
        String sceneType = paramMap.get("sceneType").toString();
        List<PolicySceneActivity> activityList = policySceneMapper.getPolicySceneList(paramMap);

        if( sceneType.equals("1") ) {
            for (PolicySceneActivity activity : activityList) {
                List<String> canUploaders = policySceneMapper.getPolicyUploaders("1");
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
    public int getPolicySceneListTotals(Map paramMap){
        return policySceneMapper.getPolicySceneListTotals(paramMap);
    }

    @Override
    public int isCanUp(Map paramMap){
        return policySceneMapper.isCanUp(paramMap);
    }

    @Override
    public void uploadActivity(Map parameterMap){
        policySceneMapper.uploadActivity(parameterMap);
    }

    @Override
    public void deleteSceneActivity(Map parameterMap) {
        policySceneMapper.deleteSceneActivity(parameterMap);
    }

    @Override
    public PolicySceneActivity getSceneActivityDetail(String activityId){
        PolicySceneActivity activityBean = policySceneMapper.getActivityDetail(activityId);
        //获取kpiBO活动效果评估指标
        PolicySceneKpiBO kpiBO = policySceneMapper.getPolicySceneKpiBO(activityId);
        activityBean.setKpiBO(kpiBO);
        //获取子活动信息
        List<PolicySceneCampaignBO> campaignBOList = policySceneMapper.getPolicySceneCampaignBO(activityId);
        for (PolicySceneCampaignBO campaignBO : campaignBOList){
            String campaignId = campaignBO.getCampaignId();
            //获取子活动channelBO渠道信息
            PolicySceneChannelBO channelBO = policySceneMapper.getPolicySceneCamChannelBO(campaignId);
            campaignBO.setChannelBO(channelBO);
            //获取子活动segmentBO目标客户群
            PolicySceneSegmentBO segmentBO = policySceneMapper.getPolicySceneCamsegmentBO(campaignId);
            campaignBO.setSegmentBO(segmentBO);
            //获取子活动timeBO时机策略
            PolicySceneTimeBO timeBO = policySceneMapper.getPolicySceneCamTimeBO(campaignId);
            campaignBO.setTimeBO(timeBO);
            //获取子活动List<OfferBO>产品
            List<PolicySceneOffer> offerList = policySceneMapper.getPolicySceneCamOffer(campaignId);
            campaignBO.setOfferList(offerList);
        }
        activityBean.setCampaignList(campaignBOList);
        return  activityBean;
    };

    @Override
    public List<Map<String,String>> getAllPolicySceneList(Map parameterMap){
        String type = parameterMap.get("sceneType").toString();
        List<Map<String,String >> allList = policySceneMapper.getAllPolicySceneList(parameterMap);

        for( Map row:allList ){
            String activityType = row.get("ACTIVITYTYPE").toString();
            if( type.equals("1") ){
                if( row.get("FLAG") != null ){
                    String flag = row.get("FLAG").toString();
                    if( flag.equals("1") ){
                        row.put("FLAG","保存待上报");
                    }else if(flag.equals("2") || flag.equals("10")){
                        row.put("FLAG","上报中");
                    }else if(flag.equals("4")){
                        row.put("FLAG","上报成功");
                    }else{
                        row.put("FLAG","上报失败");
                    }
                }
            }else{
                row.put("FLAG","集团审批通过并发布");
            }

            if( row.get("ACTIVITYOBJECTIVE") != null ){
                String objectiveCode = row.get("ACTIVITYOBJECTIVE").toString();
                row.put("ACTIVITYOBJECTIVE",ActivityObjectiveEnum.getName(objectiveCode));
            }
            if( activityType.equals("6") ){
                row.put("ISPCC","是");
                row.put("ACTIVITYTYPE","PCC策略");
            }else{
                row.put("ISPCC","否");
                if( activityType.equals("1") ){
                    row.put("ACTIVITYTYPE","4G产品类");
                }else if(activityType.equals("2")){
                    row.put("ACTIVITYTYPE","终端类");
                }else if(activityType.equals("3")){
                    row.put("ACTIVITYTYPE","流量类");
                }else if(activityType.equals("4")){
                    row.put("ACTIVITYTYPE","数字化服务类");
                }else if(activityType.equals("5")){
                    row.put("ACTIVITYTYPE","基础服务类");
                }else if(activityType.equals("7")){
                    row.put("ACTIVITYTYPE","宽带类");
                }else if(activityType.equals("9")){
                    row.put("ACTIVITYTYPE","其他类");
                }
            }


            //子活动
            String activityId = row.get("ACTIVITYID").toString();
            //获取kpiBO活动效果评估指标
            PolicySceneKpiBO kpiBO = policySceneMapper.getPolicySceneKpiBO(activityId);
            JSONObject commonKpiMap = JSONObject.fromObject(kpiBO.getCommonKpiMap().toUpperCase());

            row.putAll(commonKpiMap);
            //获取子活动信息
            List<PolicySceneCampaignBO> campaignBOList = policySceneMapper.getPolicySceneCampaignBO(activityId);
            for (PolicySceneCampaignBO campaignBO : campaignBOList){
                String campaignId = campaignBO.getCampaignId();
                //获取子活动channelBO渠道信息
                PolicySceneChannelBO channelBO = policySceneMapper.getPolicySceneCamChannelBO(campaignId);
                String channelId = channelBO.getChannelId();
                if( channelId != "" && channelId != null ){
                    channelBO.setChannelId(ChannelEnum.getName(channelId));
                }
                row.putAll(this.convertBean2Map(channelBO));
                //获取子活动segmentBO目标客户群
                PolicySceneSegmentBO segmentBO = policySceneMapper.getPolicySceneCamsegmentBO(campaignId);
                row.putAll(this.convertBean2Map(segmentBO));
                //获取子活动timeBO时机策略
                PolicySceneTimeBO timeBO = policySceneMapper.getPolicySceneCamTimeBO(campaignId);
                String timeId = timeBO.getTimeId();
                String timeRuleCode = timeBO.getTimeRule();
                if( timeId != "" && timeId != null ){
                    timeBO.setTimeId(TimeTypeEnum.getName(timeId));
                }
                if( timeRuleCode != "" && timeRuleCode != null ){
                    timeBO.setTimeRule(TimeRuleEnum.getName(timeRuleCode));
                }

                row.putAll(this.convertBean2Map(timeBO));
                //获取子活动List<OfferBO>产品
                List<PolicySceneOffer> offerList = policySceneMapper.getPolicySceneCamOffer(campaignId);
                for (PolicySceneOffer offer : offerList){
                    String offerTypeCode = offer.getOfferType();
                    if( offerTypeCode != "" && offerTypeCode != null ) {
                        offer.setOfferType(OfferTypeEnum.getName(offerTypeCode));
                    }
                    row.putAll(this.convertBean2Map(offer));
                }
            }

        }
        return allList;
    }

    @Override
    public Map<String,Object> getActivityEvalutionIndexs(String activityId){
        return policySceneMapper.getActivityEvalutionIndexs(activityId);
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

}
