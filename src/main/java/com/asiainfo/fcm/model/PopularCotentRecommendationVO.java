package com.asiainfo.fcm.model;

public class PopularCotentRecommendationVO {
    //活动标识
    private String activityId;
    //推荐类型，值为1
    private int type;
    //计算是否完成，如果返回false，则表示计算未完成，还未产生最终推荐结果
    private boolean completed;

    private ContentRecommendations recommendations;

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

    public ContentRecommendations getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(ContentRecommendations recommendations) {
        this.recommendations = recommendations;
    }
}
