package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IPccService;
import com.asiainfo.fcm.util.HttpUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by PuMg on 2017/8/2/0002.
 */
@RestController
@RequestMapping("/pcc")
public class PccController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PccController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IPccService pccService;

    @Value("${fcm.pcc.conn.url}")
    private String pccUrl;

    @Value("${fcm.pcc.userFileDir}")
    private String pccUserFileDir;

    @GetMapping("/getPccToken")
    public Result getPccToken(HttpSession session) throws Exception {
        User user = UserUtil.getCurrentUser(session);
        String str = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(str, user.getUserId());
        stringRedisTemplate.expire(str, 1800, TimeUnit.SECONDS);
        return ResultUtil.success(str);
    }

    @PostMapping("/validation")
    public Map pccValidation(@RequestParam String token) throws Exception {
        Map resultMap = new HashMap<>();
        try {
            boolean isExsit = stringRedisTemplate.hasKey(token);
            if (isExsit) {
                String userId = stringRedisTemplate.opsForValue().get(token).toString();
                resultMap.put("result", "suc");
                resultMap.put("userid", userId);
                logger.info("= = = = = token: " + token + " 校验成功！= = = = =");
                logger.info("= = = = = useId: " + userId + " 校验成功！= = = = =");
            } else {
                resultMap.put("result", "fail");
                logger.info("= = = = = token: " + token + " 校验失败！= = = = =");
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "fail");
            logger.info("= = = = = token: " + token + " 校验失败！= = = = =");
            return resultMap;

        }
    }

    /**
     *  获取pcc场景选项值
     * @param session
     * @param PolicyType 策略类型 CityId  部署地市
     * @return
     * @throws Exception
     */
    @GetMapping("/getPccSceneList")
    public Result getPccSceneList(HttpSession session,@RequestParam String PolicyType,@RequestParam(defaultValue = "") String PolicyName,@RequestParam(defaultValue = "") String PolicyCode,@RequestParam(defaultValue = "false")String ExactQuery)throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Map parameterMap = new HashMap<>();
        parameterMap.put("PolicyType",Integer.parseInt(PolicyType));
        parameterMap.put("PolicyName",PolicyName);
        parameterMap.put("PolicyCode",PolicyCode);
        parameterMap.put("ExactQuery",ExactQuery);

        HttpUtil doPost = new HttpUtil();
        String postUrl = pccUrl+"restful/policy/query?username=testInterface&password=changepassword";
        String lines = doPost.sendPost(postUrl,parameterMap);

        Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);
        return ResultUtil.success(resultMap);
    }

    @GetMapping("/activityPolicyIssued")
    public Result activityPolicyIssued(@RequestParam String activityId,@RequestParam String userFileName)throws Exception{
        Map activityPolicyInfo = pccService.getActivityPolicyInfo(activityId);
        Map parameterMap = new HashMap<>();
        parameterMap.put("ActivityCode",activityPolicyInfo.get("ACTIVITY_ID"));
        parameterMap.put("ActivityName",activityPolicyInfo.get("ACTIVITY_NAME"));
        parameterMap.put("StartTime",activityPolicyInfo.get("START_TIME")+" 00:00:00");
        parameterMap.put("EndTime",activityPolicyInfo.get("END_TIME")+" 23:59:59");
        parameterMap.put("ActivityType",Integer.parseInt(activityPolicyInfo.get("ACTIVITY_TYPE").toString()));
        parameterMap.put("PolicyCode",activityPolicyInfo.get("POLICY_CODE"));
        parameterMap.put("Description",activityPolicyInfo.get("DESCRIPTION"));
        parameterMap.put("UserFileName",userFileName);
        parameterMap.put("UserFileDir",pccUserFileDir);

        String postUrl = pccUrl+"/restful/policy/activity/receive?username=testInterface&password=changepassword";
        HttpUtil doPost = new HttpUtil();
        String lines = doPost.sendPost(postUrl,parameterMap);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);

        //记录发送日志
        Map logMap = new HashMap<>();
        logMap.put("activityId",activityId);
        logMap.put("code",resultMap.get("Code"));
        logMap.put("errorMessage",resultMap.get("ErrorMessage"));

        pccService.updateFtpState(logMap);

        return ResultUtil.success(resultMap);

    }

    /**
     * 策略活动反馈接口
     *  暂时不做文件处理
     * @return
     * @throws Exception
     */
    @GetMapping("/pccResultFeedback")
    public Result pccResultFeedback(@RequestParam String ActivityCode,@RequestParam String ActivityName,@RequestParam String UserFileName,@RequestParam String UserFileDir)throws Exception{
        Map resultMap = new HashMap<>();
        try{

            resultMap.put("Code",0);
            return ResultUtil.success(resultMap);
        }catch (Exception e){
            resultMap.put("Code",99);
            resultMap.put("ErrorMessage","其他错误");
            return ResultUtil.success(resultMap);
        }
    }




}
