package com.asiainfo.fcm.mapper.primary;

import java.util.List;
import java.util.Map;

public interface MentalMapMapper {

    void saveCustomerBuildInfo(Map map);

    List<String> getCustomerNotBuildInfo();

    void updateCustomerBuildInfo(Map map);

    Map getActivityBaseInfo(String activityId);

    void updateSourceCustomerBuildState(String activityId);

    void saveMentalMapTaskId(Map paramMap);

    List<Map> getMentalMapTaskInfo();

    void updateMentalMapTaskInfo(Map paramMap);
}
