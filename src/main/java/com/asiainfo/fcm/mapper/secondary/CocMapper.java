package com.asiainfo.fcm.mapper.secondary;

import com.asiainfo.fcm.entity.CustomerGroupDetail;
import com.asiainfo.fcm.entity.CustomerGroupInfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/29.
 */
public interface CocMapper {
    long getCocCustomerGroupsTotalRecord(Map<String, Object> map);

    List<CustomerGroupInfo> getCocCustomerGroups(Map<String, Object> map);

    CustomerGroupInfo getCocCustomerGroup(Map<String, Object> map);

    List<CustomerGroupDetail> getCocCustomerGroupDetail(Map<String, Object> map);

    Timestamp getCocTimestamp();
}
