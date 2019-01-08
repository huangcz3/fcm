package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.service.ICocService;
import com.asiainfo.fcm.util.PasswordUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

/**
 * Created by RUOK on 2017/7/9.
 */
@RestController
@RequestMapping("/Coc")
public class CocController {

    private static final Logger logger = LoggerFactory.getLogger(CocController.class);

    @Value("${coc.login.url.prefix}")
    private String cocLoginUrlPreFix;

    @Autowired
    private ICocService cocService;

    @GetMapping("/loginUrl")
    public Result getCocLoginUrl(HttpSession session) throws Exception {
        User user = UserUtil.getCurrentUser(session);

        StringBuilder stringBuilder = new StringBuilder();

        Timestamp timestamp = cocService.getCocTimestamp();

        stringBuilder.append("loginNo=");
        stringBuilder.append(user.getUserId());

        stringBuilder.append("&pwd=");
        stringBuilder.append(user.getPassword());

        stringBuilder.append("&op_time=");
        stringBuilder.append(timestamp.toString());

        String param = PasswordUtil.encrypt(stringBuilder.toString());

        return ResultUtil.success(cocLoginUrlPreFix + param);
    }

}
