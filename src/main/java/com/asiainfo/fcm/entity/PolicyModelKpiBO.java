package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public class PolicyModelKpiBO {
    private String modelId="";

    private String transformRatio="";

    private String increaseRatio="";

    private String otherSelfKpi="";

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTransformRatio() {
        return transformRatio;
    }

    public void setTransformRatio(String transformRatio) {
        this.transformRatio = transformRatio;
    }

    public String getIncreaseRatio() {
        return increaseRatio;
    }

    public void setIncreaseRatio(String increaseRatio) {
        this.increaseRatio = increaseRatio;
    }

    public String getOtherSelfKpi() {
        return otherSelfKpi;
    }

    public void setOtherSelfKpi(String otherSelfKpi) {
        this.otherSelfKpi = otherSelfKpi;
    }
}
