package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IUserService;
import com.asiainfo.fcm.util.DateUtil;
import com.asiainfo.fcm.util.PasswordUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by RUOK on 2017/7/10.
 */
@Controller
@RequestMapping("/verifyUser")
public class VerifyController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

    @Autowired
    private IUserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String method = "/verifyUser/?PN=";

    @Value("${fcm.login.effective_interval}")
    private long effectiveInterval ;

    private static String PRE = "CNT";

    @GetMapping("/")
    public void verifyUser(HttpSession session, HttpServletResponse response, @RequestParam("PN") String encryptedParam) throws IOException {
        try {
            boolean isExist = stringRedisTemplate.hasKey(encryptedParam);
            if(!isExist){
                String decryptedParam = PasswordUtil.decrypt(encryptedParam);

                String params[] = decryptedParam.split("&");
                String loginNo = params[0].split("=")[1];
                User user = userService.queryUser(loginNo);

                String tmpTime = params[2].split("=")[1];
                long opTime = DateUtil.StringToLong(tmpTime);

                long currentTime = System.currentTimeMillis();
                boolean isTrueTime = Math.abs(currentTime - opTime) <= effectiveInterval * 60 * 1000 ? true : false ;

                logger.info("effectiveInterval is -> {},login_id is {},login time is {} -> toLong {},systerm time is {} -> toLong {} ",effectiveInterval,user.getUserId(),tmpTime ,opTime,DateUtil.getTimeYYYYMMDDHHDDSS(),currentTime);

                if(user != null && isTrueTime){
                    user.setPN(encryptedParam);
                    session.setAttribute("user", user);

                    stringRedisTemplate.opsForValue().set(encryptedParam,"1");
                    stringRedisTemplate.expire(encryptedParam,effectiveInterval, TimeUnit.MINUTES);

                    response.sendRedirect("/fcm/vicActManage.html");
                }else{
                    response.sendRedirect("/fcm/error_info.html");
                }
            }else{
                logger.error("用户登录异常,使用重复链接");
                response.sendRedirect("/fcm/error_info.html");
            }
        } catch (Exception e) {
            logger.error("用户登录异常", e);
            response.sendRedirect("/fcm/error_info.html");
        }
    }

    @GetMapping("/otherBrowser")
    public void verifyOnOtherBrowser(HttpSession session, HttpServletResponse response, @RequestParam("PN") String pnUUID) throws Exception {

        User user = UserUtil.getCurrentUser(session);
        if (user == null) {
            logger.info("pnUUID:-> {}", pnUUID);
            boolean isExsit = stringRedisTemplate.hasKey(pnUUID);
            Long isExpired = stringRedisTemplate.getExpire(pnUUID);

            if (!isExsit || isExpired == -2) {
                logger.info("用户信息：-> {} ,链接已失效！", pnUUID);
                response.sendRedirect("/fcm/error_info.html");
            } else {
                boolean isFirstLogin = stringRedisTemplate.hasKey(PRE+pnUUID);
                if(!isFirstLogin){
                    String encrypt = stringRedisTemplate.opsForValue().get(pnUUID);
                    if(encrypt != null ){
                        logger.info("encrypt: {}", encrypt);
                        String decryptedParam = PasswordUtil.decrypt(encrypt);

                        String params[] = decryptedParam.split("&");
                        String loginNo = params[0].split("=")[1];

                        User CurrentUserInfo = userService.queryUser(loginNo);
                        CurrentUserInfo.setPN(encrypt);
                        logger.info("用户：-> {} ,已切换浏览器登录成功！", pnUUID);
                        session.setAttribute("user", CurrentUserInfo);

                        stringRedisTemplate.opsForValue().set(PRE+pnUUID,"1");

                        response.sendRedirect("/fcm/vicActManage.html");
                    }else{
                        response.sendRedirect("/fcm/error_info.html");
                    }
                }else{
                    logger.info("用户：-> {} ,切换浏览器,使用相同链接重复登录 !!!", pnUUID);
                    response.sendRedirect("/fcm/error_info.html");
                }
            }
        } else {
            response.sendRedirect("/fcm/error_info.html");
        }
    }

    @GetMapping("/getLoginInfo")
    @ResponseBody
    public String getLoginInfo(HttpServletRequest request, @RequestParam String loginNo, @RequestParam String loginType)throws Exception {

        User user = userService.queryUser(loginNo);
        String str = "loginno="+user.getUserId()+"&pwd="+user.getPassword()+"&op_time="+DateUtil.getTimemillisecond();
        String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath() + method;

        return url + PasswordUtil.encrypt(str);
    }

}
