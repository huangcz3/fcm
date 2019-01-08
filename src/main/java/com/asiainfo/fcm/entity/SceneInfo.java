package com.asiainfo.fcm.entity;

/**
 * Created by PuMg on 2017/6/26/0026.
 * 场景维表
 */
public class SceneInfo {
    //场景ID
    private int sceneId;
    //场景名称
    private String sceneName;
    //是否有效
    private int effective;

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public int getEffective() {
        return effective;
    }

    public void setEffective(int effective) {
        this.effective = effective;
    }
}
