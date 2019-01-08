package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.PccMapper;
import com.asiainfo.fcm.service.IPccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by PuMg on 2017/8/18/0018.
 */
@Service
public class PccServiceImpl implements IPccService {
    @Autowired
    private PccMapper pccMapper;

    @Override
    public Map getActivityPolicyInfo(String activityId) {
        return pccMapper.getActivityPolicyInfo(activityId);
    }

    @Override
    public void updateFtpState(Map map) {
        pccMapper.updateFtpState(map);
    }


}
