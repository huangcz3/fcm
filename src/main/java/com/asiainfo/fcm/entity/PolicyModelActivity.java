package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public class PolicyModelActivity {
    private String modelId="";

    private String uploadDateTime="";

    private String proId="";

    private String modelName="";

    private String modelType="";

    private String modelSimpleDesc="";

    private String createTime="";

    private String updateTime="";

    private String modelObjective="";

    private String businessDesc="";

    private String businessPerson="";

    private String objectiveUserNum="";

    private String resultType="";

    private String caseSence="";

    private String modelTool="";

    private String handwareReq="";

    private String softRun="";

    private String outputFormat="";

    private String exampleData="";

    private String coreMethod="";

    private String paramPersume="";

    private String modelDesc="";

    private String modelOutput="";

    private String modelFile="";

    private String modelAttrMap="";

    private String createId="";

    private String createName="";

    private String uploadId="";

    private String uploadName="";

    private String proName="";

    /**
     * 数据描述对象
     */
    private PolicyModelDataDescBO dataDesc;
    /**
     * 数据预处理对象
     */
    private PolicyModelDataHandleBO dataHandle;
    /**
     * 模型评估指标对象
     */
    private PolicyModelModeEvaBO modeEva;
    /**
     * 业务效果评估对象
     */
    private PolicyModelKpiBO modelKpiBO;

    private String type="";

    private String flag="";

    private String isOk="";

    private String isCanUpload="";

    private String isCanDelete="";


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(String uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelSimpleDesc() {
        return modelSimpleDesc;
    }

    public void setModelSimpleDesc(String modelSimpleDesc) {
        this.modelSimpleDesc = modelSimpleDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModelObjective() {
        return modelObjective;
    }

    public void setModelObjective(String modelObjective) {
        this.modelObjective = modelObjective;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getBusinessPerson() {
        return businessPerson;
    }

    public void setBusinessPerson(String businessPerson) {
        this.businessPerson = businessPerson;
    }

    public String getObjectiveUserNum() {
        return objectiveUserNum;
    }

    public void setObjectiveUserNum(String objectiveUserNum) {
        this.objectiveUserNum = objectiveUserNum;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getCaseSence() {
        return caseSence;
    }

    public void setCaseSence(String caseSence) {
        this.caseSence = caseSence;
    }

    public String getModelTool() {
        return modelTool;
    }

    public void setModelTool(String modelTool) {
        this.modelTool = modelTool;
    }

    public String getHandwareReq() {
        return handwareReq;
    }

    public void setHandwareReq(String handwareReq) {
        this.handwareReq = handwareReq;
    }

    public String getSoftRun() {
        return softRun;
    }

    public void setSoftRun(String softRun) {
        this.softRun = softRun;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getExampleData() {
        return exampleData;
    }

    public void setExampleData(String exampleData) {
        this.exampleData = exampleData;
    }

    public String getCoreMethod() {
        return coreMethod;
    }

    public void setCoreMethod(String coreMethod) {
        this.coreMethod = coreMethod;
    }

    public String getParamPersume() {
        return paramPersume;
    }

    public void setParamPersume(String paramPersume) {
        this.paramPersume = paramPersume;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public String getModelOutput() {
        return modelOutput;
    }

    public void setModelOutput(String modelOutput) {
        this.modelOutput = modelOutput;
    }

    public String getModelFile() {
        return modelFile;
    }

    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
    }

    public String getModelAttrMap() {
        return modelAttrMap;
    }

    public void setModelAttrMap(String modelAttrMap) {
        this.modelAttrMap = modelAttrMap;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIsOk() {
        return isOk;
    }

    public void setIsOk(String isOk) {
        this.isOk = isOk;
    }

    public String getIsCanUpload() {
        return isCanUpload;
    }

    public void setIsCanUpload(String isCanUpload) {
        this.isCanUpload = isCanUpload;
    }

    public PolicyModelDataDescBO getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(PolicyModelDataDescBO dataDesc) {
        this.dataDesc = dataDesc;
    }

    public PolicyModelDataHandleBO getDataHandle() {
        return dataHandle;
    }

    public void setDataHandle(PolicyModelDataHandleBO dataHandle) {
        this.dataHandle = dataHandle;
    }

    public PolicyModelModeEvaBO getModeEva() {
        return modeEva;
    }

    public void setModeEva(PolicyModelModeEvaBO modeEva) {
        this.modeEva = modeEva;
    }

    public PolicyModelKpiBO getModelKpiBO() {
        return modelKpiBO;
    }

    public void setModelKpiBO(PolicyModelKpiBO modelKpiBO) {
        this.modelKpiBO = modelKpiBO;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getIsCanDelete() {
        return isCanDelete;
    }

    public void setIsCanDelete(String isCanDelete) {
        this.isCanDelete = isCanDelete;
    }
}
