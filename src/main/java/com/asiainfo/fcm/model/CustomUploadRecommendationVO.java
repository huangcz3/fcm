package com.asiainfo.fcm.model;

public class CustomUploadRecommendationVO {
    //活动标识
    private String activityId;
    //推荐类型，值为3
    private int type;
    //计算是否完成，如果返回false，则表示计算未完成，还未产生最终推荐结果
    private boolean completed;
    //上传文件名称
    private String fileName;
    //用户群有效用户数
    private int amount;
    //结果输出地址
    private String outputPath;

    private UserRecommendations recommendations;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public UserRecommendations getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(UserRecommendations recommendations) {
        this.recommendations = recommendations;
    }
}
