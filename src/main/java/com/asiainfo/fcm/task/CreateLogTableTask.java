package com.asiainfo.fcm.task;

import com.asiainfo.fcm.service.IManageService;
import com.asiainfo.fcm.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class CreateLogTableTask {

    private Logger logger = LoggerFactory.getLogger(CreateLogTableTask.class);

    @Value("${fcm.customer-group.tablespace}")
    private String tableSpace;

    @Autowired
    private IManageService manageService;

    @Scheduled(cron = "0 0 20,23 * * ?")
    public void createLogTable() {
        try {
            LocalDate localDate = LocalDate.now();
            LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());

            if (localDate.isEqual(lastDayOfMonth)) {
                String nextMonthYYYYMM = DateUtil.getNextMonthYYYYMMString();

                int count = manageService.isLogTableExists(nextMonthYYYYMM);

                if (count == 0) {
                    logger.info("创建日志表开始！");
                    manageService.createLogTable(nextMonthYYYYMM, tableSpace);
                    logger.info("创建日志表结束！");
                }
            }
        } catch (Exception e) {
            logger.error("创建日志表异常！", e);
        }
    }

}
