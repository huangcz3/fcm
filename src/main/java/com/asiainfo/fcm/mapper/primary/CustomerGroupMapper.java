package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.Activity;
import com.asiainfo.fcm.entity.ActivityCustomerGroupInfo;
import com.asiainfo.fcm.entity.CustomerGroupInfo;
import com.asiainfo.fcm.entity.CustomerRemoveInfo;
import com.asiainfo.fcm.model.ActivityCustomerUpdateInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/28.
 */
public interface CustomerGroupMapper {
    void saveCustomerGroupInfo(CustomerGroupInfo customerGroupInfo);

    void updateActivityCustomerGroupInfo(@Param("activityId") String activityId, @Param("customerGroupId") String customerGroupId, @Param("cocGroupId") String cocGroupId);

    void createCustomerGroupTable(Map<String, Object> map);

    void saveCustomerGroupDetail(Map<String, Object> map);

    int distinctCustomerGroupDetail(Map<String, Object> map);

    long getCustomerGroupsTotalRecord(Map<String, Object> map);

    List<CustomerGroupInfo> getCustomerGroups(Map<String, Object> map);

    CustomerGroupInfo getCustomerGroup(ActivityCustomerGroupInfo activityCustomerGroupInfo);

    int unionCustomerGroups(Map<String, Object> map);

    void saveCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo);

    int removeCustomers(Map<String, Object> map);

    void updateCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo);

    void dropTmpTable(String tmpTableName);

    List<ActivityCustomerUpdateInfo> getCustomerGroupForUpdateByCycle();

    int diffCustomerGroup(Map<String, Object> map);

    List<Activity> getNotCompletedMiguContent();

    void updateMiguContent(CustomerGroupInfo customerGroupInfo);

    void cycleUpdateCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo);

    void updateCocGroupDateTime(@Param("customerGroupId") String customerGroupId, @Param("dataTime") String dataTime);

    void insertCustomerGroupTable(Map<String, String> paramMap);

    void insertRediskmUserList(Map<String, String> paramMap);

    void insertRediskmActiveList(Map<String, String> paramMap);

    //更新智能地图活动客户群信息表的客户群id
    void updateActCustomerGroupInfo(@Param("customerGroupId") String customerGroupId, @Param("activityId") String activityId);

    List<Activity> getNotCompletedCustomerInsightActivity();

    void saveChannelSuggestBaseInfo(Map map);

    int getChannelSuggestListTotalsByCreateIdAndQueryStr(Map map);

    List<Map> getChannelSuggestListByCreateIdAndQueryStr(Map map);

    void updateChanelSuggestRecord(Map<String, Object> paramMap);

    void calculateChannelSuggestCustomer(Map paramMap);

    List<Map> getChannelSuggestById(String customerGroupId);

    List<Map> getChannelSuggestCountByTableName(String tableName);

    List<Map> getChannelSuggestByChannel(@Param("tableName") String tableName, @Param("channel")String channel);

}
