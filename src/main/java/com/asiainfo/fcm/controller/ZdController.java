package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.util.CryptAES;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 驻点顾问：单点登录
 * Created by Administrator on 2017/11/28.
 */

@RestController
@RequestMapping("/zd")
public class ZdController {

    private static final Logger logger = LoggerFactory.getLogger(ZdController.class);

    @Value("${fcm.zd.conn.url}")
    private String zdUrl;

    private static final String KEY = "WADS#EDC!QAZ@WSX";

    private static final int MAX_TIME = 20;

    /**
     * 主动校验
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/driving")
    public Result driVerify(HttpSession session) throws IOException {

        User user = UserUtil.getCurrentUser(session);
        if(user != null){
            long stamp = System.currentTimeMillis();
            String plainText = user.getUserId() + "&" + String.valueOf(stamp);
            String encryptedParam = CryptAES.AES_Encrypt(KEY,plainText);

            logger.info("stamp {}",stamp);
            logger.info("plainText {}",plainText);
            logger.info("encryptedParam {}",encryptedParam);

            return ResultUtil.success(URLEncoder.encode(encryptedParam,"utf-8"));
        }else {
            return ResultUtil.error(ResultEnum.NOT_LOGIN);
        }

    }

    /**
     * 被动校验
     * @param session
     * @param response
     * @param encryptedParam
     * @throws IOException
     */
    @GetMapping("/passive")
    public Result pasVerify(HttpSession session, HttpServletResponse response,
                          @RequestParam("param") String encryptedParam) throws IOException {
        logger.info("encryptedParam: {}", encryptedParam);
        User user = UserUtil.getCurrentUser(session);
        String params[] = CryptAES.AES_Decrypt(KEY, URLDecoder.decode(encryptedParam, "UTF-8")).split("&");

        if (params != null && params.length == 2) {

            String loginNo = params[0];
            String oglTime = params[1];
            //s为原时间戳和当前时间戳中间相隔的分钟数
            Long diffTime = (System.currentTimeMillis() - Long.parseLong(oglTime)) / (1000 * 60);
            String url;

            if (diffTime > MAX_TIME) {
                logger.info("login timeOut ");
                return ResultUtil.error(ResultEnum.NOT_LOGIN);
            }

            logger.info("loginNo: {},diffTime: {}", loginNo,diffTime);

            if (loginNo != null && loginNo.equals(user.getUserId())) {
                url = URLEncoder.encode(CryptAES.AES_Encrypt(KEY, zdUrl),"utf-8");
                logger.info("url {}", url);
                response.sendRedirect(zdUrl + "index/loginPcApi?url=" + url);
                return ResultUtil.success();
            } else {
                return ResultUtil.error(ResultEnum.NOT_LOGIN);
            }
        } else {
            return ResultUtil.error(ResultEnum.NO_VALID_DATA);
        }

    }
}
