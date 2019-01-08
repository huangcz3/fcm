package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.entity.BlackWhiteRedUserInfo;
import com.asiainfo.fcm.entity.ChannelInfo;
import com.asiainfo.fcm.entity.OperationLog;
import com.asiainfo.fcm.mapper.primary.ManageMapper;
import com.asiainfo.fcm.service.IManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/10.
 */
@Service
@Transactional(readOnly = true)
public class ManageServiceImpl implements IManageService {

    @Autowired
    private ManageMapper manageMapper;

    @Override
    public void addApprover(ApproverInfo approverInfo) {
        manageMapper.addApprover(approverInfo);
    }

    @Override
    public List<Map<String, String>> queryCustomerType(String[] phoneNos) {
        return manageMapper.queryCustomerType(phoneNos);
    }

    @Override
    public void addBlackWhiteRedUser(BlackWhiteRedUserInfo blackWhiteRedUserInfo) {
        manageMapper.addBlackWhiteRedUser(blackWhiteRedUserInfo);
    }

    @Override
    public int isExsitByPhoneNo(BlackWhiteRedUserInfo BlackWhiteRedUserInfo) {
        return manageMapper.isExsitByPhoneNo(BlackWhiteRedUserInfo);
    }

    @Override
    public List<ApproverInfo> queryAllApprover() {
        return manageMapper.queryAllApprover();
    }

    /**
     * 获取审批人列表
     *
     * @param parameterMap
     * @return
     */
    @Override
    public List<ApproverInfo> getApproverInfoList(Map parameterMap) {
        return manageMapper.getApproverInfoList(parameterMap);
    }

    /**
     * 获取审批人总人数
     *
     * @param paramaterMap
     * @return
     */
    @Override
    public int getApproverTotalRecords(Map paramaterMap) {
        return manageMapper.getApproverTotalRecords(paramaterMap);
    }

    /**
     * 更改审批人信息
     *
     * @param approverInfo
     */
    @Override
    public void setApprover(ApproverInfo approverInfo) {
        manageMapper.setApprover(approverInfo);
    }

    @Override
    public void deleteApprover(Map paramaterMap) {
        manageMapper.deleteApprover(paramaterMap);
    }

    @Override
    public Integer approverIsUnique(ApproverInfo approverInfo) {
        return manageMapper.approverIsUnique(approverInfo);
    }

    @Override
    public int approverUpadateIsUnique(ApproverInfo approverInfo) {
        return manageMapper.approverUpadateIsUnique(approverInfo);
    }

    @Override
    public List<ChannelInfo> getChannelInfoList(Map parameterMap) {
        return manageMapper.getChannelInfoList(parameterMap);
    }

    @Override
    public int getChannelRecords(Map parameterMap) {
        return manageMapper.getChannelRecords(parameterMap);
    }

    @Override
    public void setChannelInfo(ChannelInfo channelInfo) {
        manageMapper.setChannelInfo(channelInfo);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOperationLog(String currentMonthYYYYMM, OperationLog operationLog) {
        manageMapper.saveOperationLog(currentMonthYYYYMM, operationLog);
    }

    @Override
    public int isLogTableExists(String nextMonthYYYYMM) {
        return manageMapper.isLogTableExists(nextMonthYYYYMM);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createLogTable(String nextMonthYYYYMM, String tableSpace) {
        manageMapper.createLogTable(nextMonthYYYYMM, tableSpace);
    }

    @Override
    public List<String> getLevelList(String channelId) {
        List<String> levelList = manageMapper.getLevelList(channelId);
        return levelList;
    }

    @Override
    public List<Map> getCityList(String channelId,String level) {
        Map map = new HashMap();
        map.put("channelId",channelId);
        map.put("level",level);
        List<Map> list = manageMapper.getCityList(map);
        return list;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void createCustomerGroupTable(String tableName, String tableSpace) {
        manageMapper.createCustomerGroupTable(tableName, tableSpace);
    }
}
