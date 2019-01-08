package com.asiainfo.fcm.mapper.tertiary;

import com.asiainfo.fcm.entity.User;

/**
 * Created by RUOK on 2017/6/26.
 */
public interface UserMapper {
    User queryUser(String loginNo);
}
