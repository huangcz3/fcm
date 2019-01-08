package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.service.ICustomerInsightService;
import com.asiainfo.fcm.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuJh on 2018/3/8.
 */
@Service
public class CustomerInsightServiceImpl implements ICustomerInsightService {

    @Value("${customer-insight.host}")
    private String customerInsightHost;

    @Value("${customer-insight.port}")
    private String customerInsightPort;

    @Override
    @Async
    public void getUserGroup(String activityId, String batchIds) {
        Map map = new HashMap();
        map.put("batchIds", batchIds);
        map.put("activity_id", activityId);
        HttpUtil.sendGet("http://" + customerInsightHost + ":" + customerInsightPort + "/pm_insight/build/getFTPFile", map);
    }
}
