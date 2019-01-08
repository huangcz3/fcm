package com.asiainfo.fcm.task;

import com.asiainfo.fcm.controller.CustomerInsightController;
import com.asiainfo.fcm.controller.MiguContentConsoleController;
import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.CustomerGroupInfo;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.model.ActivityChannelDetail;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.service.ITaskService;
import com.asiainfo.fcm.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by RUOK on 2017/7/5.
 */
@Component
public class ActivityTask {

    private static final Logger logger = LoggerFactory.getLogger(ActivityTask.class);

    @Autowired
    private CustomerInsightController customerInsightController;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private IUserService userService;

    @Autowired
    private MiguContentConsoleController miguContentConsoleController;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IActivityService activityService;

    @Value("fcm.toutiao.api.url")
    private String toutiaoApiUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateMiguContent() {

        logger.info("开始查询未计算完成的数字内容活动！");

        List<Activity> activityList = customerGroupService.getNotCompletedMiguContent();
        for (Activity activity : activityList) {

            logger.info("查询活动{}", activity.getActivityId());

            User user = userService.queryUser(activity.getCreatorId());

            try {
                Map map = miguContentConsoleController.getMiguContentConsoleToken(user);
                boolean success = Boolean.valueOf(String.valueOf(map.get("success")));
                if (success) {
                    String token = String.valueOf(map.get("result"));
                    Map pageResult = miguContentConsoleController.pageResults(activity.getActivityId(), token, 1, 1);

                    JSONObject jsonObject = JSONObject.fromObject(pageResult);

                    boolean isSuccess = jsonObject.getBoolean("success");

                    if (isSuccess) {
                        JSONObject resultJson = jsonObject.getJSONObject("result");
                        boolean completed = resultJson.getBoolean("completed");
                        if (completed) {
                            int type = resultJson.getInt("type");
                            long amount = resultJson.getLong("amount");
                            String outputPath = resultJson.getString("outputPath");

                            CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();
                            customerGroupInfo.setAmount(amount);
                            customerGroupInfo.setMiguContentOutputpath(outputPath);
                            customerGroupInfo.setMiguContentId(activity.getActivityId());

                            JSONObject recommendations = resultJson.getJSONObject("recommendations");
                            JSONArray dataList = recommendations.getJSONArray("data");
                            if (type == 2 && dataList.size() > 0) {
                                JSONObject data = dataList.getJSONObject(0);
                                String miguContentMarketing = data.getString("description");
                                String miguContentShorturl = data.getString("shortUrl");
                                customerGroupInfo.setMiguContentMarketing(miguContentMarketing);
                                customerGroupInfo.setMiguContentShorturl(miguContentShorturl);
                            }

                            customerGroupService.updateMiguContent(customerGroupInfo);

                            logger.info("活动{}数字内容计算完成！", activity.getActivityId());
                        }
                    } else {
                        logger.warn("查询活动{}数字内容时获取结果失败！", activity.getActivityId());
                    }

                } else {
                    logger.warn("查询活动{}数字内容时获取token失败！", activity.getActivityId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateCoustomerInsightActivity() {

        logger.info("开始查询未计算完成客户洞察活动");
        List<Activity> activity = customerGroupService.getNotCompletedCustomerInsightActivity();
        for (int i = 0; i < activity.size(); i++) {
            customerInsightController.getDetailInfo(activity.get(i).getActivityId());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Scheduled(cron = "0 0 0,8,12,18 * * ?")
    public void updateActivityCustomerTask() {
        logger.info("开始扫描日更新客户群===");

        asyncTask.updateActivityCustomer();
    }

    @Async
    public void recordOpLog(Map map) {
        logger.info("= = 记录活动操作日志");
        taskService.recordOpLog(map);
    }

    //@Scheduled(cron = "0 0/5 * * * ?")
    public void putAd2Toutiao() {
        List<String> activityIdList = activityService.getActivities2Toutiao();

        for (String activityId : activityIdList) {
            String activityJsonString = stringRedisTemplate.opsForValue().get("ACTIVE-BASEINFO:" + activityId);
            JSONObject activityJson = JSONObject.fromObject(activityJsonString);
            Activity activity = (Activity) JSONObject.toBean(activityJson);

            List<ActivityChannelDetail> activityChannelDetailList = activity.getActivityChannelDetailList();

            activityChannelDetailList
                    .stream()
                    .filter(a -> "q08".equals(a.getChannelId()))
                    .findFirst()
                    .ifPresent(b -> {
                        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
                        multiValueMap.add("activityId", b.getActivityId());

                        b.getActivityChannelRuleDetailList().forEach(
                                c -> {
                                    if ("R011".equals(c.getRuleId())) {
                                        multiValueMap.add("title", c.getRuleValue());
                                    } else if ("R006".equals(c.getRuleId())) {
                                        multiValueMap.add("imgUrl", c.getRuleValue());
                                    } else if ("R007".equals(c.getRuleId())) {
                                        multiValueMap.add("clickThroughUrl", c.getRuleValue());
                                    }
                                }
                        );

                        RestTemplate restTemplate = new RestTemplate();
                        String url = toutiaoApiUrl + "/toutiao/put";
                        String result = restTemplate.postForObject(url, multiValueMap, String.class);

                        JSONObject jsonObject = JSONObject.fromObject(result);

                        String status = jsonObject.getString("status");
                        if ("success".equals(status)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("dsp_ad");
                            String innerStatus = jsonArray.getJSONObject(0).getString("status");
                            if ("success".equals(innerStatus)) {
                                stringRedisTemplate.opsForSet().add("SET:PUT2TOUTIAO", activityId);
                                logger.info("头条活动：{}送审成功！", activityId);
                            } else {
                                String msg = jsonArray.getJSONObject(0).getString("msg");
                                logger.info("头条活动：{}送审失败，{}", activityId, msg);

                                Map<String, Object> map = new HashMap<>();
                                map.put("activityId", activityId);
                                map.put("channelId", "q08");
                                map.put("approverId", "ai_lujianguo");
                                map.put("preLevel", 2);
                                map.put("approvalResult", 2);
                                map.put("approvalComments", msg);

                                activityService.saveApprovalByBackground(map);
                            }
                        } else {
                            String msg = jsonObject.getString("msg");
                            logger.info("头条活动：{}送审失败，{}", activityId, msg);

                            Map<String, Object> map = new HashMap<>();
                            map.put("activityId", activityId);
                            map.put("channelId", "q08");
                            map.put("approverId", "ai_lujianguo");
                            map.put("preLevel", 2);
                            map.put("approvalResult", 2);
                            map.put("approvalComments", msg);

                            activityService.saveApprovalByBackground(map);
                        }
                    });
        }
    }

   // @Scheduled(cron = "0 0/5 * * * ?")
    public void getAdStatusFromToutiao() {
        Set<String> activityIds = stringRedisTemplate.opsForSet().members("SET:PUT2TOUTIAO");
        for (String activityId : activityIds) {
            MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("activityId", activityId);
            multiValueMap.add("status", "approved");

            RestTemplate restTemplate = new RestTemplate();
            String url = toutiaoApiUrl + "/toutiao/put";
            String result = restTemplate.postForObject(url, multiValueMap, String.class);

            JSONObject jsonObject = JSONObject.fromObject(result);

            String status = jsonObject.getString("status");
            if ("success".equals(status)) {
                JSONArray jsonArray = jsonObject.getJSONArray("dsp_ad");
                if (jsonArray.size() > 0) {
                    logger.info("头条活动：{}审核通过！", activityId);

                    Map<String, Object> map = new HashMap<>();
                    map.put("activityId", activityId);
                    map.put("channelId", "q08");
                    map.put("approverId", "ai_lujianguo");
                    map.put("preLevel", 2);
                    map.put("approvalResult", 1);

                    activityService.saveApprovalByBackground(map);

                    stringRedisTemplate.opsForSet().remove("SET:PUT2TOUTIAO", activityId);
                } else {
                    multiValueMap.remove("status");
                    multiValueMap.add("status", "refused");
                    String refusedResult = restTemplate.postForObject(url, multiValueMap, String.class);

                    JSONObject refusedJsonObject = JSONObject.fromObject(refusedResult);

                    String refusedStatus = refusedJsonObject.getString("status");

                    if ("success".equals(refusedStatus)) {
                        JSONArray refusedJsonArray = jsonObject.getJSONArray("dsp_ad");
                        if (refusedJsonArray.size() > 0) {
                            JSONObject refusedAd = refusedJsonArray.getJSONObject(0);
                            String reason = refusedAd.getString("reason");

                            logger.info("头条活动：{}审核被拒绝！{}", activityId, reason);

                            Map<String, Object> map = new HashMap<>();
                            map.put("activityId", activityId);
                            map.put("channelId", "q08");
                            map.put("approverId", "ai_lujianguo");
                            map.put("preLevel", 2);
                            map.put("approvalResult", 2);
                            map.put("approvalComments", reason);

                            activityService.saveApprovalByBackground(map);

                            stringRedisTemplate.opsForSet().remove("SET:PUT2TOUTIAO", activityId);
                        }
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkWechatStatus() {
        List<String> activityIdList = activityService.getActivities2Wechat();
        Set<String> passedSet = stringRedisTemplate.opsForSet().members("WECHATPASSED");
        Set<String> rejectedSet = stringRedisTemplate.opsForSet().members("WECHATREJECTED");
        for (String activityId : activityIdList) {
            if (passedSet.contains(activityId)) {
                //通过
                logger.info("微信朋友圈活动：{}审核通过！", activityId);

                Map<String, Object> map = new HashMap<>();
                map.put("activityId", activityId);
                map.put("channelId", "q11");
                map.put("approverId", "ai_lujianguo");
                map.put("preLevel", 2);
                map.put("approvalResult", 1);

                activityService.saveApprovalByBackground(map);
                stringRedisTemplate.opsForSet().remove("WECHATPASSED", activityId);

                RestTemplate restTemplate = new RestTemplate();
                String url = toutiaoApiUrl + "/tsa/update/" + activityId;
                MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
                multiValueMap.add("configuredStatus", "AD_STATUS_NORMAL");

                restTemplate.postForObject(url, multiValueMap, String.class);

            } else if (rejectedSet.contains(activityId)) {
                //未通过
                String rejectMessage = stringRedisTemplate.opsForValue().get("WECHATREJECTEDMSG:" + activityId);

                logger.info("微信朋友圈活动：{}审核被拒绝！{}", activityId, rejectMessage);

                Map<String, Object> map = new HashMap<>();
                map.put("activityId", activityId);
                map.put("channelId", "q11");
                map.put("approverId", "ai_lujianguo");
                map.put("preLevel", 2);
                map.put("approvalResult", 2);
                map.put("approvalComments", rejectMessage);

                activityService.saveApprovalByBackground(map);
                stringRedisTemplate.opsForSet().remove("WECHATPASSED", activityId);
                stringRedisTemplate.delete("WECHATREJECTEDMSG:" + activityId);
            }
        }
    }

}
