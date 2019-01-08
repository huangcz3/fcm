package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.mapper.primary.OnlineBehaviorMapper;
import com.asiainfo.fcm.model.OnlineBehaviorInfo;
import com.asiainfo.fcm.model.RuleDataTransfer;
import com.asiainfo.fcm.service.IOnlineBehaviorTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by PuMg on 2017/8/14/0014.
 */
@Service
public class OnlineBehaviorTaskServiceImpl implements IOnlineBehaviorTaskService {

    private static final Logger logger = LoggerFactory.getLogger(OnlineBehaviorTaskServiceImpl.class);

    @Autowired
    private OnlineBehaviorMapper onlineBehaviorMapper;

    @Override
    public List<OnlineBehaviorInfo> getOnlineBehaviorInfo() {
        return onlineBehaviorMapper.getOnlineBehaviorInfo();
    }

    @Override
    public int isExsitBehavioInfo(String activityId) {
        return onlineBehaviorMapper.isExsitBehavioInfo(activityId);
    }

    @Override
    @Transactional(readOnly = false , rollbackFor = Exception.class )
    public void saveBehaviorInfo() {
        List<OnlineBehaviorInfo> onlineBehaviorInfoList = onlineBehaviorMapper.getOnlineBehaviorInfo();
        for(OnlineBehaviorInfo behaviorInfo : onlineBehaviorInfoList){
            String activityId = behaviorInfo.getActivityId();
            int isExsit = onlineBehaviorMapper.isExsitBehavioInfo(activityId);
            if(isExsit == 0 ){
                logger.info("= = = = = 开始保存上网行为信息 = = = = =");
                //活动信息入surfing_active_info
                String startTime = behaviorInfo.getEffTime()+" 00:00:00";
                String endTime = behaviorInfo.getExpTime()+" 00:00:00";
                behaviorInfo.setEffTime(behaviorInfo.getEffTime().replaceAll("-",""));
                behaviorInfo.setExpTime(behaviorInfo.getExpTime().replaceAll("-",""));
                onlineBehaviorMapper.saveOnlineBehaviorActivityInfo(behaviorInfo);
                logger.info("= = = = = 活动id: "+activityId+" 上网行为信息存入surfing_active_info表成功 = = = = =");

                //规则入表rule_data_transfer
                RuleDataTransfer transfer = new RuleDataTransfer();
                String fields = "busi_id in " + behaviorInfo.getSceneValue();
                transfer.setFields(fields);
                transfer.setRuleId("qcd_"+behaviorInfo.getActivityId());
                transfer.setEventId("event_gn_app");
                transfer.setStartTime(Timestamp.valueOf(startTime));
                transfer.setEndTime(Timestamp.valueOf(endTime));
                transfer.setGroupKey("SET-USERLIST:"+behaviorInfo.getActivityId());
                transfer.setState("0");
                onlineBehaviorMapper.saveOnlineBehaviorRuleData(transfer);
                logger.info("= = = = = 规则id: "+transfer.getRuleId()+" 上网行为规则信息存入rule_data_transfer 成功 = = = = =");
            }
        }
    }
}
