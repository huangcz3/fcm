package com.asiainfo.fcm.service;

        import com.asiainfo.fcm.entity.ApproverInfo;
        import com.asiainfo.fcm.entity.BlackWhiteRedUserInfo;
        import com.asiainfo.fcm.entity.ChannelInfo;
        import com.asiainfo.fcm.entity.OperationLog;

        import java.util.List;
        import java.util.Map;

/**
 * Created by RUOK on 2017/7/10.
 */
public interface IManageService {
    void addApprover(ApproverInfo approverInfo);

    List<Map<String, String>> queryCustomerType(String[] phoneNos);

    void addBlackWhiteRedUser(BlackWhiteRedUserInfo blackWhiteRedUserInfo);

    int isExsitByPhoneNo(BlackWhiteRedUserInfo blackWhiteRedUserInfo);

    List<ApproverInfo> queryAllApprover();

    List<ApproverInfo> getApproverInfoList(Map parameterMap);

    int getApproverTotalRecords(Map paramaterMap);

    void setApprover(ApproverInfo approverInfo);

    void deleteApprover(Map paramaterMap);

    Integer approverIsUnique(ApproverInfo approverInfo);

    int approverUpadateIsUnique(ApproverInfo approverInfo);

    List<ChannelInfo> getChannelInfoList(Map parameterMap);

    int getChannelRecords(Map parameterMap);

    void setChannelInfo(ChannelInfo channelInfo);

    void saveOperationLog(String currentMonthYYYYMM, OperationLog operationLog);

    int isLogTableExists(String nextMonthYYYYMM);

    void createLogTable(String nextMonthYYYYMM, String tableSpace);

    List<String> getLevelList(String channelId);

    List<Map> getCityList(String channelId,String level);

    void createCustomerGroupTable(String tableName, String tableSpace);
}
