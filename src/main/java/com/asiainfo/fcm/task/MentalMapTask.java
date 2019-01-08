package com.asiainfo.fcm.task;

import com.asiainfo.fcm.service.IMentalMapService;
import com.asiainfo.fcm.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by PuMg on 2017/9/5/0005.
 */
@Component
public class MentalMapTask {

    private Logger logger = LoggerFactory.getLogger(MentalMapTask.class);

    @Autowired
    private IMentalMapService mentalMapService;

    @Value("${fcm.mentalMap.url}")
    private String mentalMapUrl;

    /**
     * 异步方法 智能地图端 保存活动
     *
     * @param activityId        活动id
     * @param taskId            请求智能地图端的活动id
     * @param taskName          活动名称
     * @param startTime         活动开始时间
     * @param endTime           活动结束时间
     * @param customerGroupName 客户群名称
     */
    @Async
    public void mentalMapSaveActivity(String activityId,
                                      String taskId,
                                      String taskName,
                                      String startTime,
                                      String endTime,
                                      String customerGroupName) {
        logger.info("= = = = = 智能地图，智能地图保存活动 = = = = =");
        //拼接参数
        Map mentalMap = new HashMap<>();
        mentalMap.put("taskId", taskId);
        mentalMap.put("taskName", taskName);
        mentalMap.put("startTime", startTime.replaceAll("-", "") + "000000");
        mentalMap.put("endTime", endTime.replaceAll("-", "") + "235959");

        String mapUrl = mentalMapUrl + "activity/save";
        HttpUtil doGet = new HttpUtil();
        Map<Object, Object> resultMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        //捕获异常，一旦存在异常，立刻重新请求保存接口
        try {
            String lines = doGet.sendGet(mapUrl, mentalMap);
            resultMap = objectMapper.readValue(lines, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //休眠5s再重新请求
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String re_lines = doGet.sendGet(mapUrl, mentalMap);
            try {
                resultMap = objectMapper.readValue(re_lines, Map.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        int code = (int) resultMap.get("code");
        //请求成功后，保存taskId
        if (code == 200) {

            Map paramMap = new HashMap();
            paramMap.put("activityId", activityId);
            paramMap.put("activityName", taskName);
            paramMap.put("taskId", taskId);
            paramMap.put("customerGroupName", customerGroupName);

            mentalMapService.saveMentalMapTaskId(paramMap);

            logger.info("活动{}反馈成功！", activityId);
        } else {
            logger.error("保存活动失败！" + resultMap.get("data"));
        }
    }

    /**
     * 定时请求 智能地图端 用户群是否生成成功
     *
     * @return
     * @throws Exception
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void mentalMapIsComplete() throws Exception {
        logger.info("= = = = = 智能地图，请求用户群是否生成定时任务 = = = = =");

        List<Map> activityList = mentalMapService.getMentalMapTaskInfo();
        if (activityList != null) {
            for (int i = 0; i < activityList.size(); i++) {
                Map map = activityList.get(i);
                String taskId = (String) map.get("TASK_ID");
                String activityId = (String) map.get("ACTIVITY_ID");
                String customerGroupName = (String) map.get("CUSTOMER_GROUP_NAME");

                String mapUrl = mentalMapUrl + "/activity/isComplete";

                HttpUtil doGet = new HttpUtil();
                Map paramMap = new HashMap<>();
                paramMap.put("taskId", taskId);

                String lines = doGet.sendGet(mapUrl, paramMap);

                ObjectMapper objectMapper = new ObjectMapper();
                Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);

                //完成客户群计算 更新表状态
                Map map1 = (Map) resultMap.get("data");
                if (1 == Integer.parseInt(map1.get("isComplete").toString())) {
                    mentalMapService.updateMentalMapTaskInfo(paramMap);
                    logger.info("活动：" + activityId + ",名为‘" + customerGroupName + "’用户群计算完成");
                }
            }
        } else {
            logger.info("= = = = = 暂无智能地图目标用户是否生成请求任务 = = = = =");
        }
    }


    /**
     * 定时请求 智能地图端 用户群是否生成成功
     *
     * @throws Exception
     */
//    @Scheduled(cron = "0 0/5 * * * ?")
//    public void isComplete() throws Exception {
//        logger.info("= = = = = 智能地图，请求用户群是否生成定时任务 = = = = =");
//
//        List<String> ids = mentalMapService.getCustomerNotBuildInfo();
//        if (ids != null) {
//            for (int i = 0; i < ids.size(); i++) {
//                String activityId = ids.get(i);
//
//                String mapUrl = mentalMapUrl + "/activity/isComplete";
//
//                HttpUtil doGet = new HttpUtil();
//                Map paramMap = new HashMap<>();
//                paramMap.put("taskId", activityId);
//                String lines = doGet.sendGet(mapUrl, paramMap);
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);
//
//                //完成客户群计算 更新表状态
//                Map map = (Map) resultMap.get("data");
//                if (1 == Integer.parseInt(map.get("isComplete").toString())) {
//                    paramMap.put("filePath", map.get("filePath"));
//                    mentalMapService.updateCustomerBuildInfo(paramMap);
//                } else {
//                    logger.info("= = = = = 活动：" + activityId + "用户群未完成计算 = = = = =");
//                }
//            }
//        } else {
//            logger.info("= = = = = 暂无智能地图目标用户是否生成请求任务 = = = = =");
//        }
//    }

}
