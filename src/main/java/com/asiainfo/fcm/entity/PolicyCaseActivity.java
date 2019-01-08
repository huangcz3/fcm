package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/12.
 */
public class PolicyCaseActivity {
    private String caseId="";

    private String upDateTime="";

    private String proId="";

    private String proName="";

    private String title="";

    private String keyword="";

    private String desc="";

    private String caseObjective="";

    private String mind="";

    private String keyPoint="";

    private String kpi="";

    private String result="";

    private String caseAttrMap="";

    private String attachmentId="";

    /**
     * 场景类型：1:省级上传；2：策略库下发
     */
    private String caseType="";
    /**
     * 本省活动状态；1:保存待上传；2:本省已上传 4：上传iop失败； 5：上传iop成功；
     */
    private String flag="";
    /**
     *   活动是否有效；0:无效; 1:有效
     */
    private String isOK="";

    private String createId="";

    private String createName="";

    private String createTime="";

    private String uploadId="";

    private String uploadName="";

    private String isCanUpload="";

    private String isCanDelete = "";

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(String upDateTime) {
        this.upDateTime = upDateTime;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCaseObjective() {
        return caseObjective;
    }

    public void setCaseObjective(String caseObjective) {
        this.caseObjective = caseObjective;
    }

    public String getMind() {
        return mind;
    }

    public void setMind(String mind) {
        this.mind = mind;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCaseAttrMap() {
        return caseAttrMap;
    }

    public void setCaseAttrMap(String caseAttrMap) {
        this.caseAttrMap = caseAttrMap;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIsOK() {
        return isOK;
    }

    public void setIsOK(String isOK) {
        this.isOK = isOK;
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

    public String getIsCanUpload() {
        return isCanUpload;
    }

    public void setIsCanUpload(String isCanUpload) {
        this.isCanUpload = isCanUpload;
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
