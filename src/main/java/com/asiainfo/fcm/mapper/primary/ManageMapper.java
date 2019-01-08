package com.asiainfo.fcm.mapper.primary;

import com.asiainfo.fcm.entity.ApproverInfo;
import com.asiainfo.fcm.entity.BlackWhiteRedUserInfo;
import com.asiainfo.fcm.entity.ChannelInfo;
import com.asiainfo.fcm.entity.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/7/10.
 */
public interface ManageMapper {
    void addApprover(ApproverInfo approverInfo);

    List<Map<String, String>> queryCustomerType(String[] phoneNos);

    void addBlackWhiteRedUser(BlackWhiteRedUserInfo blackWhiteRedUserInfo);

    int isExsitByPhoneNo(BlackWhiteRedUserInfo blackWhiteRedUserInfoo);

    List<ApproverInfo> queryAllApprover();

    List<ApproverInfo> getApproverInfoList(Map parameterMap);

    int getApproverTotalRecords(Map paramaterMap);

    void setApprover(ApproverInfo approverInfo);

    void deleteApprover(Map paramaterMap);

    int approverIsUnique(ApproverInfo approverInfo);

    int approverUpadateIsUnique(ApproverInfo approverInfo);

    List<ChannelInfo> getChannelInfoList(Map parameterMap);

    int getChannelRecords(Map parameterMap);

    void setChannelInfo(ChannelInfo channelInfo);

    void saveOperationLog(@Param("currentMonthYYYYMM") String currentMonthYYYYMM, @Param("operationLog") OperationLog operationLog);

    int isLogTableExists(String nextMonthYYYYMM);

    void createLogTable(@Param("nextMonthYYYYMM") String nextMonthYYYYMM, @Param("tableSpace") String tableSpace);

    List<String> getLevelList(String channelId);

    List<Map> getCityList(String channelId);

    List<Map> getCityList(Map map);

    void createCustomerGroupTable(@Param("tableName") String tableName, @Param("tableSpace") String tableSpace);
}
