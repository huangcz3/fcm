package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.ActivityOpRecordInfo;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.SmsOpEnum;
import com.asiainfo.fcm.mapper.primary.ActivityOpRecordInfoMapper;
import com.asiainfo.fcm.service.ISmsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by chengzhongtao on 2017/12/15.
 */
@Service
@Transactional
public class SmsManageImpl implements ISmsManageService {

    @Autowired
    private ActivityOpRecordInfoMapper activityOpRecordInfoMapper;

    @Override
    public List<ActivityOpRecordInfo> getOperateList(String activityId) {
        return activityOpRecordInfoMapper.getAllSmsOperate(activityId);
    }

    @Override
    public void saveOperateInfo(String activityId, User user, int opType, SmsOpEnum parse, String reason, int rel) {
        ActivityOpRecordInfo aop = new ActivityOpRecordInfo();
        aop.setActivityId(activityId);
        aop.setUserId(user.getUserId());
        aop.setUserName(user.getUserName());
        aop.setUserPhone("");
        aop.setOpType(opType);
        aop.setOpCode(parse.getCode());
        aop.setOpName(parse.getMsg());
        aop.setOpDesc(reason);
        aop.setOpResult(rel);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());
        aop.setOpTime(nowTime);
        activityOpRecordInfoMapper.saveOperateInfo(aop);
    }
}
