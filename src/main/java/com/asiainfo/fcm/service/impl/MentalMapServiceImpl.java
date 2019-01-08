package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivitySceneInfo;
import com.asiainfo.fcm.mapper.primary.ActivityMapper;
import com.asiainfo.fcm.mapper.primary.MentalMapMapper;
import com.asiainfo.fcm.service.IMentalMapService;
import com.asiainfo.fcm.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/9/6/0006.
 * 智能地图
 */

@Service
public class MentalMapServiceImpl implements IMentalMapService {

    @Value("${fcm.mentalMap.url}")
    private String mentalMapUrl;

    @Value("${fcm.mentalMap.userFileDir}")
    private String userFileDir;

    @Autowired
    private MentalMapMapper mentalMapMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public Map saveMentalMapActivityInfo(Map map) throws IOException {

        String activityId = map.get("activityId").toString();
        Activity activityBean = activityMapper.getActivityById(activityId);

        List<ActivitySceneInfo> activitySceneInfoList = activityMapper.getSceneInfo(activityId);

        Map mentalMap = new HashMap<>();
//        mentalMap.put("passbyLimitTime","");
//        mentalMap.put("usersType","");
        long count = activitySceneInfoList.stream().filter(s -> "SR013".equals(s.getSceneRuleId()) || "SR014".equals(s.getSceneRuleId())).count();
        if(count > 0) {
            activitySceneInfoList.stream().forEach(s -> {
                if ("SR013".equals(s.getSceneRuleId())) {
//                    mentalMap.put("passbyLimitTime", s.getSceneRuleValue());
                } else {
//                    mentalMap.put("usersType",s.getSceneRuleValue());
                }
            });
        }
        mentalMap.put("taskId",activityBean.getActivityId());
        mentalMap.put("taskName",activityBean.getActivityName());
        mentalMap.put("startTime",activityBean.getStartTime().replaceAll("-","")+"000000");
        mentalMap.put("endTime",activityBean.getEndTime().replaceAll("-","")+"235959");
//        mentalMap.put("userFilePath",map.get("userFilePath"));

        String mapUrl = mentalMapUrl+"/activity/save";
        HttpUtil doGet = new HttpUtil();
        String lines = doGet.sendGet(mapUrl,mentalMap);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);

        return resultMap;
    }

    @Override
    public void saveCustomerBuildInfo(Map map) {
        mentalMapMapper.saveCustomerBuildInfo(map);
    }

    @Override
    public List<String> getCustomerNotBuildInfo() {
        return mentalMapMapper.getCustomerNotBuildInfo();
    }

    @Override
    public void updateCustomerBuildInfo(Map map) {
        mentalMapMapper.updateCustomerBuildInfo(map);
    }

    @Override
    public Map getActivityBaseInfo(String activityId) {

        return mentalMapMapper.getActivityBaseInfo(activityId);
    }

    @Override
    public void updateSourceCustomerBuildState(String activityId) {
        mentalMapMapper.updateSourceCustomerBuildState(activityId);
    }

    @Override
    public void saveMentalMapTaskId(Map paramMap) {
        mentalMapMapper.saveMentalMapTaskId(paramMap);
    }

    @Override
    public List<Map> getMentalMapTaskInfo() {
        return mentalMapMapper.getMentalMapTaskInfo();
    }

    @Override
    public void updateMentalMapTaskInfo(Map paramMap) {
        mentalMapMapper.updateMentalMapTaskInfo(paramMap);
    }
}
