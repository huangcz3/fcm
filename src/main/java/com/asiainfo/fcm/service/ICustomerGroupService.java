package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.ActivityCustomerUpdateInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/28.
 */
public interface ICustomerGroupService {
    void saveCustomerGroupInfo(CustomerGroupInfo customerGroupInfo);

    void updateActivityCustomerGroupInfo(String activityId, String customerGroupId, String cocGroupId);

    void createCustomerGroupTable(CustomerGroupInfo customerGroupInfo);

    void saveCustomerGroupDetail(Map<String, Object> map);

    int distinctCustomerGroupDetail(Map<String, Object> map);

    long getCocCustomerGroupsTotalRecord(Map<String, Object> map);

    List<CustomerGroupInfo> getCocCustomerGroups(Map<String, Object> map);

    long getCustomerGroupsTotalRecord(Map<String, Object> map);

    List<CustomerGroupInfo> getCustomerGroups(Map<String, Object> map);

    CustomerGroupInfo getCocCustomerGroup(Map<String, Object> map);

    List<CustomerGroupDetail> getCocCustomerGroupDetail(Map<String, Object> map);

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

    void updateCocGroupDateTime(String customerGroupId, String dataTime);

    //更新智能地图活动客户群信息表的客户群id
    void updateActCustomerGroupInfo(String customerGroupId,String activityId);

    void insertCustomerGroupTable(Map<String, String> paramMap);

    void insertRediskm(Map<String, String> paramMap);

    List<Activity> getNotCompletedCustomerInsightActivity();

    void saveChannelSuggestBaseInfo(Map map);

    List<Map> getChannelSuggestListByCreateIdAndQueryStr(Map map);

    int getChannelSuggestListTotalsByCreateIdAndQueryStr(Map map);

    void updateChanelSuggestRecord(Map<String, Object> paramMap);

    void calculateChannelSuggestCustomer(Map map);

    List<Map> getChannelSuggestById(String customerGroupId);

    List<Map> getChannelSuggestCountByTableName(String tableName);

    List<Map> getChannelSuggestByChannel(String tableName, String channel);
}
