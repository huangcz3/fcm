package com.asiainfo.fcm.model;

import java.util.List;

public class TagSelectionRecommendationVO {
    //活动标识
    private String activityId;
    //推荐类型，值为2
    private int type;
    //计算是否完成，如果返回false，则表示计算未完成，还未产生最终推荐结果
    private boolean completed;
    //所选标签描述
    private List<String> selectedTags;
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

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = selectedTags;
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
