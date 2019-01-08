package com.asiainfo.fcm.service.impl;

import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.mapper.primary.UserRoleMapper;
import com.asiainfo.fcm.mapper.tertiary.UserMapper;
import com.asiainfo.fcm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by RUOK on 2017/6/26.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(value = "tertiaryTransactionManager", readOnly = true, rollbackFor = Exception.class)
    public User queryUser(String loginNo) {
        User user = userMapper.queryUser(loginNo);
        //设置用户权限
        List<String> roles = userRoleMapper.getUserRoleInfo(loginNo);
        if(roles.isEmpty() || roles == null){
            roles.add("user");
        }
        user.setUserRole(String.join(",",roles));

        //设置用户所属部门
        List<Map> depts = userRoleMapper.getUserDepartment(loginNo);
        if(depts.size()>0){
            user.setDeptId(depts.get(0).get("DEPT_ID").toString());
            user.setDeptName(depts.get(0).get("DEPT_NAME").toString());
        }else{
            user.setDeptId("all");
            user.setDeptName("all");
        }

        return user;
    }

}
