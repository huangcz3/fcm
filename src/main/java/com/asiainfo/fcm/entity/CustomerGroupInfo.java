package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * Description：客户群信息表
 */
public class CustomerGroupInfo {

    //客户群id
    private String customerGroupId;
    //客户群名
    private String customerGroupName;
    //客户群表名
    private String groupTableName;
    //创建人id
    private String creatorId;
    //创建人姓名
    private String creatorName;
    //创建时间
    private String createTime;
    //创建方式：1标签库，2数据集市，3文件上传，4数字内容
    private int createType;
    //客户数量
    private long amount;
    //是否自定义：0否，1是
    private int customizeFlag;
    //是否有效：0无效，1有效
    private int effective;
    //是否剔除用客户群：0否，1是
    private int removeFlag;
    //标签库客户群id
    private String cocGroupId;
    //标签库客户群名
    private String cocGroupName;
    //标签库客户群表名
    private String cocTableName;
    //标签库客户群周期：1一次性，2月周期，3日周期
    private int cocGroupCycle;
    //集市数据库名
    private String mppDbName;
    //集市schema
    private String mppSchema;
    //集市表名
    private String mppTableName;
    //手机号字段名
    private String mppPhoneColumn;
    //个性化营销语字段名
    private String mppMarketingColumn;
    //文件名
    private String fileName;
    //原始文件名
    private String originalFileName;
    //原始文件记录数
    private long fileLineCount;
    //数字内容id
    private String miguContentId;
    //数字内容名称
    private String miguContentName;
    //数字内容类型：1内容推荐用户，2用户推荐内容-标签，3用户推荐内容-上传
    private int miguContentType;
    //数字内容营销语
    private String miguContentMarketing;
    //数字内容短链接地址
    private String miguContentShorturl;
    //数字内容计算是否完成
    private int miguContentCompleted;
    //数字内容结果输出地址
    private String miguContentOutputpath;
    //生成时间
    private String dataTime;
    /**
     * 数据状态：0未加载，1已加载完成
     */
    private int dataState;

    public String getCustomerGroupId() {
        return customerGroupId;
    }

    public void setCustomerGroupId(String customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public String getCustomerGroupName() {
        return customerGroupName;
    }

    public void setCustomerGroupName(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

    public String getGroupTableName() {
        return groupTableName;
    }

    public void setGroupTableName(String groupTableName) {
        this.groupTableName = groupTableName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCreateType() {
        return createType;
    }

    public void setCreateType(int createType) {
        this.createType = createType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getCustomizeFlag() {
        return customizeFlag;
    }

    public void setCustomizeFlag(int customizeFlag) {
        this.customizeFlag = customizeFlag;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }

    public int getRemoveFlag() {
        return removeFlag;
    }

    public void setRemoveFlag(int removeFlag) {
        this.removeFlag = removeFlag;
    }

    public String getCocGroupId() {
        return cocGroupId;
    }

    public void setCocGroupId(String cocGroupId) {
        this.cocGroupId = cocGroupId;
    }

    public String getCocGroupName() {
        return cocGroupName;
    }

    public void setCocGroupName(String cocGroupName) {
        this.cocGroupName = cocGroupName;
    }

    public String getCocTableName() {
        return cocTableName;
    }

    public void setCocTableName(String cocTableName) {
        this.cocTableName = cocTableName;
    }

    public int getCocGroupCycle() {
        return cocGroupCycle;
    }

    public void setCocGroupCycle(int cocGroupCycle) {
        this.cocGroupCycle = cocGroupCycle;
    }

    public String getMppDbName() {
        return mppDbName;
    }

    public void setMppDbName(String mppDbName) {
        this.mppDbName = mppDbName;
    }

    public String getMppSchema() {
        return mppSchema;
    }

    public void setMppSchema(String mppSchema) {
        this.mppSchema = mppSchema;
    }

    public String getMppTableName() {
        return mppTableName;
    }

    public void setMppTableName(String mppTableName) {
        this.mppTableName = mppTableName;
    }

    public String getMppPhoneColumn() {
        return mppPhoneColumn;
    }

    public void setMppPhoneColumn(String mppPhoneColumn) {
        this.mppPhoneColumn = mppPhoneColumn;
    }

    public String getMppMarketingColumn() {
        return mppMarketingColumn;
    }

    public void setMppMarketingColumn(String mppMarketingColumn) {
        this.mppMarketingColumn = mppMarketingColumn;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public long getFileLineCount() {
        return fileLineCount;
    }

    public void setFileLineCount(long fileLineCount) {
        this.fileLineCount = fileLineCount;
    }

    public String getMiguContentId() {
        return miguContentId;
    }

    public void setMiguContentId(String miguContentId) {
        this.miguContentId = miguContentId;
    }

    public String getMiguContentName() {
        return miguContentName;
    }

    public void setMiguContentName(String miguContentName) {
        this.miguContentName = miguContentName;
    }

    public int getMiguContentType() {
        return miguContentType;
    }

    public void setMiguContentType(int miguContentType) {
        this.miguContentType = miguContentType;
    }

    public String getMiguContentMarketing() {
        return miguContentMarketing;
    }

    public void setMiguContentMarketing(String miguContentMarketing) {
        this.miguContentMarketing = miguContentMarketing;
    }

    public String getMiguContentShorturl() {
        return miguContentShorturl;
    }

    public void setMiguContentShorturl(String miguContentShorturl) {
        this.miguContentShorturl = miguContentShorturl;
    }

    public int getMiguContentCompleted() {
        return miguContentCompleted;
    }

    public void setMiguContentCompleted(int miguContentCompleted) {
        this.miguContentCompleted = miguContentCompleted;
    }

    public String getMiguContentOutputpath() {
        return miguContentOutputpath;
    }

    public void setMiguContentOutputpath(String miguContentOutputpath) {
        this.miguContentOutputpath = miguContentOutputpath;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public int getDataState() {
        return dataState;
    }

    public void setDataState(int dataState) {
        this.dataState = dataState;
    }
}
