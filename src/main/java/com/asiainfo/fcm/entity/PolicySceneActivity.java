package com.asiainfo.fcm.entity;

import java.util.List;

/**
 * Created by ShaoJinyu on 2017/12/6.
 */
public class PolicySceneActivity {
    private String uploadId="";

    private String uploadName="";

    private String uploadTime="";

    private String createId="";

    private String createName="";

    private String createTime="";

    private String activityTemplateId="";

    private String activityId="";

    private String activityName="";

    private String activityStatus="";

    private String activityStartTime="";

    private String activityEndTime="";

    private String activityType="";

    private String activityObjective="";

    private String activityDescribe="";

    private String twicePlanRequestAcctime="";

    private String proId="";

    private String proName="";

    private String attachmentId="";

    /**
     * 营销子活动
     */
    private List<PolicySceneCampaignBO> campaignList;
    /**
     * pcc策略
     */
    private PolicyScenePccBO pccToStrategeBO;
    /**
     * 活动效果评估指标,暂时只有通用指标
     */
    private PolicySceneKpiBO kpiBO;
    /**
     *活动基本信息可扩展属性：附件名称
     */
    private String actAttrExtMap;
    /**
     * 本省活动状态；1:保存待上传；2:本省已上传 4：上传iop失败； 5：上传iop成功；
     */
    private String flag="";
    /**
     * 场景类型：1:省级上传；2：策略库下发
     */
    private String scenetype="";
    /**
     *   活动是否有效；0:无效; 1:有效
     */
    private String isok="";
    /**
     *
     *  是否能够上传；0:否；1：是
     */
    private String isCanUpload="";

    /**
     * 是否能够删除；0:否；1：是
     */
    private String isCanDelete = "";

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getActivityTemplateId() {
        return activityTemplateId;
    }

    public void setActivityTemplateId(String activityTemplateId) {
        this.activityTemplateId = activityTemplateId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityObjective() {
        return activityObjective;
    }

    public void setActivityObjective(String activityObjective) {
        this.activityObjective = activityObjective;
    }

    public String getActivityDescribe() {
        return activityDescribe;
    }

    public void setActivityDescribe(String activityDescribe) {
        this.activityDescribe = activityDescribe;
    }

    public String getTwicePlanRequestAcctime() {
        return twicePlanRequestAcctime;
    }

    public void setTwicePlanRequestAcctime(String twicePlanRequestAcctime) {
        this.twicePlanRequestAcctime = twicePlanRequestAcctime;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public List<PolicySceneCampaignBO> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<PolicySceneCampaignBO> campaignList) {
        this.campaignList = campaignList;
    }

    public PolicyScenePccBO getPccToStrategeBO() {
        return pccToStrategeBO;
    }

    public void setPccToStrategeBO(PolicyScenePccBO pccToStrategeBO) {
        this.pccToStrategeBO = pccToStrategeBO;
    }

    public PolicySceneKpiBO getKpiBO() {
        return kpiBO;
    }

    public void setKpiBO(PolicySceneKpiBO kpiBO) {
        this.kpiBO = kpiBO;
    }

    public String getActAttrExtMap() {
        return actAttrExtMap;
    }

    public void setActAttrExtMap(String actAttrExtMap) {
        this.actAttrExtMap = actAttrExtMap;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getScenetype() {
        return scenetype;
    }

    public void setScenetype(String scenetype) {
        this.scenetype = scenetype;
    }

    public String getIsok() {
        return isok;
    }

    public void setIsok(String isok) {
        this.isok = isok;
    }

    public String getIsCanUpload() {
        return isCanUpload;
    }

    public void setIsCanUpload(String isCanUpload) {
        this.isCanUpload = isCanUpload;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }


    public String getIsCanDelete() {
        return isCanDelete;
    }

    public void setIsCanDelete(String isCanDelete) {
        this.isCanDelete = isCanDelete;
    }
}

