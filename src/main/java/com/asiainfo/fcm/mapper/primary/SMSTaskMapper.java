package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.ActivityApprovalInfo;
import com.asiainfo.fcm.entity.ActivityChannelInfo;
import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.model.ActivityApprovalSmsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SMSTaskMapper {

    List<String> getNeedSmsChannels();

    List<ActivityApprovalInfo> getSMSApprovalInfo(@Param("channelIds") List<String> channelIds, @Param("preLevel") int preLevel);

    void insertSmsApproval(ActivityApprovalSmsInfo approvalSmsInfo);

    String getSmsTemplate(int level);

    int isSendedSms(Map map);

    List<ActivityApprovalSmsInfo> getUnReplySmsApprovalInfo(int validate);

    void updateSmsApprovalInfo(Map map);

    ActivityApprovalInfo getActivityApprovalResult(Map map);

    ApproverInfo getPreApproverInfo(Map map);

    ActivityChannelInfo getActivityChannelInfo(String activityId);

    List<ApproverInfo> getApproverInfo(ApproverInfo approverInfo);

    int isCanSmsApproval(Map map);
}
