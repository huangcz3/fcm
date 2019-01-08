package com.asiainfo.fcm.model;

/**
 * Created by PuMg on 2017/7/5/0005.
 * 活动审批 渠道审批明细信息
 */
public class ChannelApprovalDetail {
    //渠道ID
    private String channelId;
    //渠道名称
    private String channelName;
    //渠道审批意见
    private int approvalRsult;
    //渠道驳回内容
    private String approvalComments;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getApprovalRsult() {
        return approvalRsult;
    }

    public void setApprovalRsult(int approvalRsult) {
        this.approvalRsult = approvalRsult;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }
}
