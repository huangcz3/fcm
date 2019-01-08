package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.service.IPriorityService;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优先级管理.
 * Created by RUOK on 2017/6/26.
 */
@RestController
@RequestMapping("/priorities")
public class PriorityController {

    private static final Logger logger = LoggerFactory.getLogger(PriorityController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IPriorityService priorityService;

    @GetMapping("/")
    public Result getPriorities(HttpSession session, @RequestParam(value = "activityName", defaultValue = "") String activityName, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize) {

        User user = UserUtil.getCurrentUser(session);
        String cityId = user.getCityId();

        Map<String, Object> map = new HashMap<>();
        map.put("cityId", cityId);
        map.put("activityName", activityName);

        long totalRecord = priorityService.getPrioritiesTotalRecord(map);

        Page page = new Page(currentPage, pageSize, totalRecord);
        map.put("page", page);

        List<Activity> activityList = priorityService.getPriorities(map);

        page.setDataList(activityList);

        return ResultUtil.success(page);
    }

    @PostMapping("/")
    public Result changePriorities(HttpSession session, @RequestParam("activityId") String activityId) {

        User user = UserUtil.getCurrentUser(session);
        String cityId = user.getCityId();

        ActivityPriority activityPriority = new ActivityPriority();

        activityPriority.setActivityId(activityId);
        activityPriority.setCityId(cityId);

        priorityService.changePriorities(activityPriority);

        String isProvince = "1".equals(cityId) ? "1" : "";

        stringRedisTemplate.opsForHash().put("ACTIVE:" + activityId, "PRIORITY", isProvince + System.currentTimeMillis());

        return ResultUtil.success();
    }
}
