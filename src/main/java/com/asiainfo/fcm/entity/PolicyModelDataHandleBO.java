package com.asiainfo.fcm.entity;

/**
 * Created by ShaoJinyu on 2017/12/18.
 */
public class PolicyModelDataHandleBO {
    private String modelId="";

    private String defaultHandle="";

    private String dataUniHandle="";

    private String errorHandle="";

    private String otherHandle="";


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDefaultHandle() {
        return defaultHandle;
    }

    public void setDefaultHandle(String defaultHandle) {
        this.defaultHandle = defaultHandle;
    }

    public String getDataUniHandle() {
        return dataUniHandle;
    }

    public void setDataUniHandle(String dataUniHandle) {
        this.dataUniHandle = dataUniHandle;
    }

    public String getErrorHandle() {
        return errorHandle;
    }

    public void setErrorHandle(String errorHandle) {
        this.errorHandle = errorHandle;
    }

    public String getOtherHandle() {
        return otherHandle;
    }

    public void setOtherHandle(String otherHandle) {
        this.otherHandle = otherHandle;
    }
}
