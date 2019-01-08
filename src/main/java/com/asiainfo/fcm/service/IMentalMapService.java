package com.asiainfo.fcm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/9/6/0006.
 */
public interface IMentalMapService {

    /**
     * 保存智能地图 活动信息
     */
    Map saveMentalMapActivityInfo(Map map) throws IOException;

    /**
     * 记录活动用户群状态信息
     */
    void saveCustomerBuildInfo(Map map);

    /**
     *  获取活动用户群未生成成功的活动信息
     */
    List<String> getCustomerNotBuildInfo();

    /**
     *  更新活动目标用户群生成状态信息
     */
    void updateCustomerBuildInfo(Map map);

    /**
     * 获取活动基本信息
     */
    Map getActivityBaseInfo(String activityId);

    /**
     * 更新源目标用户群生成状态
     */
    void updateSourceCustomerBuildState(String activityId);

    /**
     * 保存智能地图taskId
     * @param paramMap
     */
    void saveMentalMapTaskId(Map paramMap);

    /**
     * 智能地图用户群未计算完成的信息
     * @return
     */
    List<Map> getMentalMapTaskInfo();

    /**
     * 更新智能地图用户群计算信息
     * @param paramMap
     */
    void updateMentalMapTaskInfo(Map paramMap);
}
