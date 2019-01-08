package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivityPriority;
import com.asiainfo.fcm.mapper.primary.PriorityMapper;
import com.asiainfo.fcm.service.IPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/27.
 */
@Service
@Transactional(readOnly = true)
public class PriorityServiceImpl implements IPriorityService {

    @Autowired
    private PriorityMapper priorityMapper;


    @Override
    public long getPrioritiesTotalRecord(Map<String, Object> map) {
        return priorityMapper.getPrioritiesTotalRecord(map);
    }

    @Override
    public List<Activity> getPriorities(Map<String, Object> map) {
        return priorityMapper.getPriorities(map);
    }

    @Override
    public void changePriorities(ActivityPriority activityPriority) {
        priorityMapper.changePriorities(activityPriority);
    }
}
