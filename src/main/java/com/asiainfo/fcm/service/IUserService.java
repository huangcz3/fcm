package com.asiainfo.fcm.service;

import com.asiainfo.fcm.entity.User;

/**
 * Created by RUOK on 2017/6/26.
 */
public interface IUserService {
    User queryUser(String loginNo);

}
