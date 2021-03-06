package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivityPriority;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/27.
 */
public interface PriorityMapper {
    long getPrioritiesTotalRecord(Map<String,Object> map);

    List<Activity> getPriorities(Map<String, Object> map);

    void changePriorities(ActivityPriority activityPriority);
}
