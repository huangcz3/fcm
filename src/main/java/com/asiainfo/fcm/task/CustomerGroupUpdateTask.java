package com.asiainfo.fcm.task;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.service.IManageService;
import com.asiainfo.fcm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujh on 2018/3/5.
 */
@Component
public class CustomerGroupUpdateTask {

    private Logger logger = LoggerFactory.getLogger(CustomerGroupUpdateTask.class);

    @Value("${fcm.customer-group.tablespace}")
    private String tableSpace;

    @Autowired
    private IManageService manageService;

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private IActivityService activityService;

    private String tableNamePrefix = "customer_group_update_record_";

    @Scheduled(cron = "0 0 23 * * ?")
    public void createLogTable() {
        try {

            String yyyymmdd = DateUtil.getNextYYYYMMDDString();

            logger.info("创建用户日更新表记录表开始！");
            manageService.createCustomerGroupTable(tableNamePrefix + yyyymmdd, tableSpace);
            logger.info("创建用户日更新表记录表结束！");

        } catch (Exception e) {
            logger.error("创建用户日更新表记录表异常！", e);
        }
    }


    @Async
    public void updateCustomerGroups(String activityId, String finalGroupTableName) {

        try {

            logger.info("活动 {} 日更新用户群到redis开始", activityId);

            // 取记录表中需要基本信息
            Activity activity = activityService.getActivityDetailInfo(activityId);
            String channelId = activity.getActivityChannelDetailList().get(0).getChannelId();
            String cityId = activity.getCityId();

            // 生成日表表名
            String tableName = tableNamePrefix + DateUtil.getCurrentYYYYMMDDString();

            // 封装参数到map中
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("activityId", activityId);
            paramMap.put("channelId", channelId);
            paramMap.put("cityId", cityId);
            paramMap.put("tableName", tableName);
            paramMap.put("finalGroupTableName", finalGroupTableName);

            customerGroupService.insertCustomerGroupTable(paramMap);

            // 封装参数插入redis扫描表
            paramMap.clear();
            paramMap.put("tableName", "FCM." + tableName);
            paramMap.put("pushCondition", "camp_id='" + activityId + "'");
            customerGroupService.insertRediskm(paramMap);

            logger.info("活动 {} 日更新用户群到redis结束", activityId);
        } catch (Exception e) {
            logger.error("日更新用户群到redis异常！", e);
        }

    }
}
