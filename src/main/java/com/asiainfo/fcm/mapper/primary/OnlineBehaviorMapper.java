package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.model.OnlineBehaviorInfo;
import com.asiainfo.fcm.model.RuleDataTransfer;

import java.util.List;

/**
 * Created by RUOK on 2017/6/29.
 */
public interface OnlineBehaviorMapper {

    List<OnlineBehaviorInfo> getOnlineBehaviorInfo();

    int isExsitBehavioInfo(String activityId);

    void saveOnlineBehaviorActivityInfo(OnlineBehaviorInfo onlineBehaviorInfo);

    void saveOnlineBehaviorRuleData(RuleDataTransfer ruleDataTransfer);
}
