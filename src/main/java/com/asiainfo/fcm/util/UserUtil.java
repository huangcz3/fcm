package com.asiainfo.fcm.util;

import com.asiainfo.fcm.entity.User;

import javax.servlet.http.HttpSession;

/**
 * Created by RUOK on 2017/6/27.
 */
public class UserUtil {
    public static User getCurrentUser(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user;
    }
}
