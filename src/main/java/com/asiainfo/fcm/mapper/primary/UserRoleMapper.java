package com.asiainfo.fcm.mapper.primary;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/29.
 */
public interface UserRoleMapper {
    List<String> getUserRoleInfo(String userId);

    List<Map> getUserDepartment(String userId);
}
