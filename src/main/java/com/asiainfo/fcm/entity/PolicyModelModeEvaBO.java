package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public class PolicyModelModeEvaBO {
    private String modelId="";

    private String coverageRatio="";

    private String accuracyRatio="";

    private String goalRatio="";

    private String otherSelfEva="";


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getCoverageRatio() {
        return coverageRatio;
    }

    public void setCoverageRatio(String coverageRatio) {
        this.coverageRatio = coverageRatio;
    }

    public String getAccuracyRatio() {
        return accuracyRatio;
    }

    public void setAccuracyRatio(String accuracyRatio) {
        this.accuracyRatio = accuracyRatio;
    }

    public String getGoalRatio() {
        return goalRatio;
    }

    public void setGoalRatio(String goalRatio) {
        this.goalRatio = goalRatio;
    }

    public String getOtherSelfEva() {
        return otherSelfEva;
    }

    public void setOtherSelfEva(String otherSelfEva) {
        this.otherSelfEva = otherSelfEva;
    }
}
