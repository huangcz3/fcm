package com.asiainfo.fcm.task;

import com.asiainfo.fcm.service.IOnlineBehaviorTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by PuMg on 2017/8/14/0014.
 * 上网行为定时任务
 */
@Component
public class OnlineBehaviorTask {

    private static final Logger logger = LoggerFactory.getLogger(OnlineBehaviorTask.class);

    @Autowired
    private IOnlineBehaviorTaskService onlineBehaviorTaskService;

    /**
     * 保存活动上网行为
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void saveOnlineBehaviorInfo(){
        onlineBehaviorTaskService.saveBehaviorInfo();
    }


}
