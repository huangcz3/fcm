package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.ActivityApproval;
import com.asiainfo.fcm.model.ActivityChannelDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/6/26/0026.
 */
public interface IActivityService {

    /**
     * 保存活动信息
     */
    void saveActivityInfo(Activity activity);

    /**
     * 保存活动全部信息
     */
    void saveActivityAllInfo(Activity activity,User user)throws Exception;

    /**
     * 保存活动渠道明细
     */
    void saveActivityChannelInfo(Map map, List<ActivityChannelDetail> list,User user);

    /**
     * 获取活动列表
     */
    List<Activity> getActivityList(Map map);

    int getActivityListTotals(Map map);

    /**
     * 获取活动详情
     */
    Activity getActivityDetailInfo(String activityId);

    /**
     * 保存推荐业务信息
     */
    void saveRecommendProductListInfo( List<ActivityRecommendProduct> activityRecommendProductList);

    /**
     * 保存活动测试号码
     */
    void saveTestPhoneNum(Map map);

    /**
     * 活动优先级设置
     */
    void saveActivityPriority(Map map);

    /**
     * 是否可以审批
     */
    int isCanApprove(Map map);

    /**
     * 保存用户群
     */
    void saveCustomGroupInfo(List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList);

    /**
     * 获取活动客户群信息
     */
    List<ActivityCustomerGroupInfo> getCustomGroupInfo(String activityId);

    /**
     * 活动审批
     */
    void approveActivityChannelInfo(ActivityApproval activityApproval,User user);

    /**
     * 获取审批人未审批的渠道信息
     */
   List<Map> getUnApprovalChannelInfo(Map map);

    /**
     * 查看当前活动是否存在内容未审批的渠道
     */
    int isExsitUnapprovalChannel(String activityId,int preLevel);

    /**
     * 更新活动状态
     *
     * @param activity
     */
    void updateActivityState(Activity activity);

    /***
     * 获取渠道详情
     */
    List<ActivityChannelDetail> getChannelDetail(String activityId);

    /**
     * 删除活动
     */
    void deleteActivity(String activityId);

    /**
     * 获取审批结果
     */
    Map getApprovaledResultInfo(String activityId,User user);

    /**
     * 校验活动是否已保存
     */
    int isExsitByActivityId(String activityId);

    /**
     * 暂停活动
     */
    void pausedAcitivity(String activityId);

    /**
     * 恢复活动
     */
    void recoveryActivity(String activityId);

    /**
     * 是否可以操作
     */
    String getCreatorId(String activityId);

    /**
     * 后台审批调用接口
     */
    void saveApprovalByBackground(Map map);

    /**
     * 是否可以操作
     */
    int isCanOp(String userId,String activityId);

    List<Map<String,String>> getAllActivityList(Map parameterMap);

    List<Map> getActivityOpLogs(String activityId);

    List<String> getActivities2Toutiao();

    List<String> getActivities2Wechat();
}

