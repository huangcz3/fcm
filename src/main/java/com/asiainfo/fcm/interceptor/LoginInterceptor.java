package com.asiainfo.fcm.interceptor;

import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IUserService;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 登录拦截器.
 * Created by RUOK on 2017/6/26.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        User user = UserUtil.getCurrentUser(session);
        if(user == null){
            ObjectMapper objectMapper = new ObjectMapper();
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            PrintWriter printWriter = response.getWriter();
            objectMapper.writeValue(printWriter, ResultUtil.error(ResultEnum.NOT_LOGIN));
            return false;
        }else{
            return true;
        }

//        if (user == null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            String encryptedParam = request.getParameter("PN");
//
//            if (StringUtils.isNotBlank(encryptedParam)) {
//                String decryptedParam = PasswordUtil.decrypt(encryptedParam);
//
//                logger.info("decryptedParam:{}", decryptedParam);
//
//                String params[] = decryptedParam.split("&");
//                String loginNo = params[0].split("=")[1];
//
//                user = userService.queryUser(loginNo);
//
//                if (user == null) {
//                    response.setHeader("Content-type", "application/json;charset=UTF-8");
//                    PrintWriter printWriter = response.getWriter();
//                    objectMapper.writeValue(printWriter, ResultUtil.error(ResultEnum.NOT_LOGIN));
//                    return false;
//                } else {
//                    session.setAttribute("user", user);
//                }
//            } else {
//                response.setHeader("Content-type", "application/json;charset=UTF-8");
//                PrintWriter printWriter = response.getWriter();
//                objectMapper.writeValue(printWriter, ResultUtil.error(ResultEnum.NOT_LOGIN));
//                return false;
//            }
//        }



    }
}
