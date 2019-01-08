package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.ActivityChannelDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by PuMg on 2017/6/27/0027.
 */
public interface ActivityMapper {
    /**
     * 保存活动信息
     */
    void saveActivityInfo(Activity activity);

    /**
     * 保存活动渠道明细
     */
    void saveActivityChannelInfo(ActivityChannelInfo activityChannelInfo);
    /**
     * 获取活动列表
     */
    List<Activity> getActivityList(Map map);

    int getActivityListTotals(Map map);

    /**
     * 获取活动基本信息
     */
    Activity getActivityById(String ActivityId);

    /**
     * 获取活动渠道信息
     */
    List<ChannelInfo> getChannelInfoByActId(String activityId);

    /**
     * 获取活动渠道详情
     */
    List<ActivityChannelDetail> getChannelDetail(String activityId);

    /**
     *  保存推荐业务信息
     */
    void saveRecommendProductListInfo( List<ActivityRecommendProduct> activityRecommendProductList);

    /**
     * 获取活动推荐业务信息
     */
    List<ActivityRecommendProduct> getRecommendProduct(String activityId);

    /**
     * 保存活动测试号码
     */
    void saveTestPhoneNum(Map map);

    /**
     * 活动优先级设置
     */
    void saveActivityPriority(Map map);

    //获取审批人信息
    ApproverInfo getApproverInfo(@Param("channelId") String channelId,@Param("approverId") String approverId,@Param("approverlevel") int level,@Param("cityId") String cityId);

    //保存活动渠道审批初始化流程
    void saveActivityApprovalInfo(ActivityApprovalInfo activityApprovalInfo);

    /**
     * 是否可以审批
     */
    int isCanApprove(Map map);

    /***
     * 获取渠道审批人
     */
    List<ApproverInfo> getApproverInfoById(@Param("channelId") String channelId, @Param("preLevel") int preLevel,@Param("cityId") String cityId);

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
    void approveActivityChannelInfo(Map map);

    /**
     * 获取当前审批人层级
     */
    int getApproverPreLevel(@Param("activityId") String activityId,@Param("channelId") String channelId,@Param("approverId") String approverId);

    /**
     * 获取渠道审批最大级别
     */
    int getApprovalMaxLevel(@Param("channelId") String channelId,@Param("cityId") String cityId);

    /**
     * 查看当前活动是否存在内容未审批的渠道
     */
    int isExsitUnapprovalChannel(@Param("activityId") String activityId,@Param("preLevel") int preLevel);

    /**
     * 获取审批人未审批的渠道信息
     */
    List<Map> getUnApprovalChannelInfo(Map map);

    /**
     * 获取测试号码
     */
    List<String> getTestPhoneList(String activityId);

    /**
     * 更新活动状态
     */
    void updteActivityState(@Param("activityId") String activityId,@Param("state") int state);

    void updateActivityState(Activity activity);

    /**
     * 获取渠道审批人层级配置信息
     */
    Map getApprovalLevelSettings(@Param("channelId") String channelId);

    /**
     * 获取渠道短信审批配置信息
     */
    List<Map> getChannelSmsSetting(@Param("channelId") String channelId,@Param("cityId") String cityId);

    /**
     * 获取渠道审批人层级配置信息
     */
    List<Map> getApprovalSetting(@Param("channelId") String channelId,@Param("cityId") String cityId);

    /**
     * 删除活动
     */
    void deleteActivity(String activityId);

    int getApprovaledMaxLevel(String activityId);
    /**
     * 查询活动状态
     */
    int getActivityState(String activityId);

    /**
     * 查询活动审批结果记录 按层级
     * @param activityId
     * @param level
     * @return
     */
    List<ActivityApprovalInfo> getActivityApprovalResult(@Param("activityId") String activityId,@Param("level") int level);

    //获取活动未审批层级（最小一级）
    int getUnApprovalMinLevel(String activityId);

    //获取当前可以审批的活动 对应审批人id
    List<String> getCanApprovalIds(@Param("activityId") String activityId,@Param("level") int level);

    List<String> getCanApprovers(@Param("activityId") String activityId,@Param("level") int level);

    //查询活动当前审批层级所有驳回记录
    int getActivityRejectTimes(@Param("activityId") String activityId,@Param("level") int level);

    //查询活动渠道数
    int getChannelTotals(String activityId);

    //获取目标用户生成明细
    CustomerRemoveInfo getCustomerRemoveInfo(String activityId);

    //保存活动场景信息
    void saveSceneInfo(List<ActivitySceneInfo> activitySceneInfoList);

    List<ActivitySceneInfo> getSceneInfo(String activityId);

    int isExsitByActivityId(String activityId);

    Activity getActivityInfoById(String activityId);

    String smsTemplateContent(int type);

    void pausedAcitivity(String activityId);

    void recoveryActivity(String activityId);

    String getCreatorId(String activityId);

    int isApprovaled(Map map);

    void saveBackgroundApprovalInfo(Map map);

    int isCanOp(@Param("userId") String userId,@Param("activityId") String activityId);

    List<String> getApproverInfos(String activityId);

    List<Map<String,String>> getAllActivityList(Map parameterMap);

    ApproverInfo getApproverInfoByApproverId(Map map);

    List<ApproverInfo> getNextApproverInfos(Map map);


    Map getSmsInfo(String activityId);

    Map getBaseInfoByActId(String activityId);

    List<Map> getActivityOpLogs(String activityId);

    int getchannelMaxLevel(@Param("channelId") String channelId,@Param("cityId") String cityId);

    List<String> getChannels(String activityId);

    List<String> getActivities2Toutiao();

    List<String> getActivities2Wechat();

}
