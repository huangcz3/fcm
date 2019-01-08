package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.ActivityApprovalInfo;
import com.asiainfo.fcm.entity.ActivityChannelInfo;
import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.mapper.primary.SMSTaskMapper;
import com.asiainfo.fcm.model.ActivityApprovalSmsInfo;
import com.asiainfo.fcm.service.ISMSTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/7/28/0028.
 */
@Service
public class SMSTaskServiceImpl implements ISMSTaskService {

    @Autowired
    private SMSTaskMapper smsTaskMapper;

    @Override
    public List<String> getNeedSmsChannels() {
        return smsTaskMapper.getNeedSmsChannels();
    }

    @Override
    public List<ActivityApprovalInfo> getSMSApprovalInfo(List<String> channelIds) {
        return smsTaskMapper.getSMSApprovalInfo(channelIds,1);
    }

    @Override
    public void insertSmsApproval(ActivityApprovalSmsInfo approvalSmsInfo) {
        smsTaskMapper.insertSmsApproval(approvalSmsInfo);
    }

    @Override
    public int isSendedSms(Map map) {
        return smsTaskMapper.isSendedSms(map);
    }

    @Override
    public String getSmsTemplate(int level) {
        return smsTaskMapper.getSmsTemplate(level);
    }

    @Override
    public List<ActivityApprovalSmsInfo> getUnReplySmsApprovalInfo(int validate) {
        return smsTaskMapper.getUnReplySmsApprovalInfo(validate);
    }

    @Override
    public void updateSmsApprovalInfo(Map map) {
        smsTaskMapper.updateSmsApprovalInfo(map);
    }

    @Override
    public ActivityApprovalInfo getActivityApprovalResult(Map map) {
        return smsTaskMapper.getActivityApprovalResult(map);
    }

    @Override
    public ApproverInfo getPreApproverInfo(Map map) {
        return smsTaskMapper.getPreApproverInfo(map);
    }

    @Override
    public ActivityChannelInfo getActivityChannelInfo(String activityId) {
        return smsTaskMapper.getActivityChannelInfo(activityId);
    }

    @Override
    public int isCanSmsApproval(Map map) {
        return smsTaskMapper.isCanSmsApproval(map);
    }

}
