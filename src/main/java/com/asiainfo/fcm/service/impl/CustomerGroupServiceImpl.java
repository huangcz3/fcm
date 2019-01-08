package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.mapper.primary.CustomerGroupMapper;
import com.asiainfo.fcm.mapper.secondary.CocMapper;
import com.asiainfo.fcm.model.ActivityCustomerUpdateInfo;
import com.asiainfo.fcm.service.ICustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/28.
 */
@Service
@Transactional(readOnly = true)
public class CustomerGroupServiceImpl implements ICustomerGroupService {

    @Autowired
    private CustomerGroupMapper customerGroupMapper;

    @Autowired
    private CocMapper cocMapper;

    @Value("${fcm.customer-group.tablespace}")
    private String tableSpace;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveCustomerGroupInfo(CustomerGroupInfo customerGroupInfo) {
        customerGroupMapper.saveCustomerGroupInfo(customerGroupInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateActivityCustomerGroupInfo(String activityId, String customerGroupId, String cocGroupId) {
        customerGroupMapper.updateActivityCustomerGroupInfo(activityId, customerGroupId, cocGroupId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createCustomerGroupTable(CustomerGroupInfo customerGroupInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerGroupInfo", customerGroupInfo);
        map.put("tableSpace", tableSpace);
        customerGroupMapper.createCustomerGroupTable(map);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveCustomerGroupDetail(Map<String, Object> map) {
        customerGroupMapper.saveCustomerGroupDetail(map);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int distinctCustomerGroupDetail(Map<String, Object> map) {
        return customerGroupMapper.distinctCustomerGroupDetail(map);
    }

    @Override
    @Transactional(value = "secondaryTransactionManager")
    public long getCocCustomerGroupsTotalRecord(Map<String, Object> map) {
        return cocMapper.getCocCustomerGroupsTotalRecord(map);
    }

    @Override
    @Transactional(value = "secondaryTransactionManager")
    public List<CustomerGroupInfo> getCocCustomerGroups(Map<String, Object> map) {
        return cocMapper.getCocCustomerGroups(map);
    }

    @Override
    public long getCustomerGroupsTotalRecord(Map<String, Object> map) {
        return customerGroupMapper.getCustomerGroupsTotalRecord(map);
    }

    @Override
    public List<CustomerGroupInfo> getCustomerGroups(Map<String, Object> map) {
        return customerGroupMapper.getCustomerGroups(map);
    }

    @Override
    @Transactional(value = "secondaryTransactionManager")
    public CustomerGroupInfo getCocCustomerGroup(Map<String, Object> map) {
        return cocMapper.getCocCustomerGroup(map);
    }

    @Override
    @Transactional(value = "secondaryTransactionManager")
    public List<CustomerGroupDetail> getCocCustomerGroupDetail(Map<String, Object> map) {
        return cocMapper.getCocCustomerGroupDetail(map);
    }

    @Override
    public CustomerGroupInfo getCustomerGroup(ActivityCustomerGroupInfo activityCustomerGroupInfo) {
        return customerGroupMapper.getCustomerGroup(activityCustomerGroupInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int unionCustomerGroups(Map<String, Object> map) {
        return customerGroupMapper.unionCustomerGroups(map);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo) {
        customerGroupMapper.saveCustomerRemoveInfo(customerRemoveInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int removeCustomers(Map<String, Object> map) {
        return customerGroupMapper.removeCustomers(map);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo) {
        customerGroupMapper.updateCustomerRemoveInfo(customerRemoveInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void dropTmpTable(String tmpTableName) {
        customerGroupMapper.dropTmpTable(tmpTableName);
    }

    @Override
    public List<ActivityCustomerUpdateInfo> getCustomerGroupForUpdateByCycle() {
        return customerGroupMapper.getCustomerGroupForUpdateByCycle();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int diffCustomerGroup(Map<String, Object> map) {
        return customerGroupMapper.diffCustomerGroup(map);
    }

    @Override
    public List<Activity> getNotCompletedMiguContent() {
        return customerGroupMapper.getNotCompletedMiguContent();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateMiguContent(CustomerGroupInfo customerGroupInfo) {
        customerGroupMapper.updateMiguContent(customerGroupInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void cycleUpdateCustomerRemoveInfo(CustomerRemoveInfo customerRemoveInfo) {
        customerGroupMapper.cycleUpdateCustomerRemoveInfo(customerRemoveInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateCocGroupDateTime(String customerGroupId, String dataTime) {
        customerGroupMapper.updateCocGroupDateTime(customerGroupId, dataTime);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void insertCustomerGroupTable(Map<String, String> paramMap) {
        customerGroupMapper.insertCustomerGroupTable(paramMap);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void insertRediskm(Map<String, String> paramMap) {
        customerGroupMapper.insertRediskmUserList(paramMap);
        customerGroupMapper.insertRediskmActiveList(paramMap);
    }

    @Override
    public List<Activity> getNotCompletedCustomerInsightActivity() {
        return customerGroupMapper.getNotCompletedCustomerInsightActivity();
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveChannelSuggestBaseInfo(Map map) {
        customerGroupMapper.saveChannelSuggestBaseInfo(map);
    }

    @Override
    public List<Map> getChannelSuggestListByCreateIdAndQueryStr(Map map) {
        return customerGroupMapper.getChannelSuggestListByCreateIdAndQueryStr(map);
    }

    @Override
    public int getChannelSuggestListTotalsByCreateIdAndQueryStr(Map map) {
        return customerGroupMapper.getChannelSuggestListTotalsByCreateIdAndQueryStr(map);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateChanelSuggestRecord(Map<String, Object> paramMap) {
        customerGroupMapper.updateChanelSuggestRecord(paramMap);
    }

    @Override
    public void calculateChannelSuggestCustomer(Map map) {
        customerGroupMapper.calculateChannelSuggestCustomer(map);
    }

    @Override
    public List<Map> getChannelSuggestById(String customerGroupId) {
        return customerGroupMapper.getChannelSuggestById(customerGroupId);
    }

    @Override
    public List<Map> getChannelSuggestCountByTableName(String tableName) {
        return customerGroupMapper.getChannelSuggestCountByTableName(tableName);
    }

    @Override
    public List<Map> getChannelSuggestByChannel(String tableName, String channel) {
        return customerGroupMapper.getChannelSuggestByChannel(tableName,channel);
    }

    @Override
    public void updateActCustomerGroupInfo(String customerGroupId, String activityId) {
        customerGroupMapper.updateActCustomerGroupInfo(customerGroupId, activityId);
    }
}
