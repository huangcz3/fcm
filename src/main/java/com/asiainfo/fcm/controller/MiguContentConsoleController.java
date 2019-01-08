package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.UserRoleEnum;
import com.asiainfo.fcm.model.CredentialInfo;
import com.asiainfo.fcm.model.PageRequest;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/miguContentConsole")
public class MiguContentConsoleController {

    private static final Logger logger = LoggerFactory.getLogger(MiguContentConsoleController.class);

    @Value("${migu-content-console.host}")
    private String miguContentConsoleHost;

    @Value("${migu-content-console.port}")
    private String miguContentConsolePort;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/token")
    public Result getMiguContentConsoleToken(HttpSession session) throws IOException {

        User user = UserUtil.getCurrentUser(session);

        Map map = this.getMiguContentConsoleToken(user);

        return ResultUtil.success(map);
    }

    public Map getMiguContentConsoleToken(User user) throws IOException {

        CredentialInfo credentialInfo = new CredentialInfo();

        credentialInfo.setUserId(user.getUserId());

        credentialInfo.setHasAuditRight(user.getUserRole().contains(UserRoleEnum.MIGU_APPROVER.getRole()));

        String currentDateTime = dateTimeFormatter.format(LocalDateTime.now());
        credentialInfo.setSsoTime(currentDateTime);

        credentialInfo.setCityId(Integer.valueOf(user.getCityId()));

        ObjectMapper objectMapper = new ObjectMapper();
        String param = objectMapper.writeValueAsString(credentialInfo);

        String path = "http://" + miguContentConsoleHost + ":" + miguContentConsolePort + "/migu-content-console/authentication/sso";

        String lines = this.doPost(path, param);

        return objectMapper.readValue(lines, Map.class);
    }

    @GetMapping("/pageResults")
    public Result pageResults(HttpSession session, @RequestParam("activityId") String activityId, @RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize) throws IOException {

        Result result = this.getMiguContentConsoleToken(session);

        String token = null;

        if (JSONObject.fromObject(result.getData()).getBoolean("success")) {
            token = JSONObject.fromObject(result.getData()).getString("result");
        }

        Map map = this.pageResults(activityId, token, currentPage, pageSize);

        return ResultUtil.success(map);
    }

    public Map pageResults(String activityId, String token, int currentPage, int pageSize) throws IOException {
        PageRequest pageRequest = new PageRequest(currentPage, pageSize);

        ObjectMapper objectMapper = new ObjectMapper();
        String param = objectMapper.writeValueAsString(pageRequest);

        String path = "http://" + miguContentConsoleHost + ":" + miguContentConsolePort + "/migu-content-console/activities/" + activityId + "/pageResults?sid=" + token;

        String lines = this.doPost(path, param);

        Map<Object, Object> map = objectMapper.readValue(lines, Map.class);//返回JSONObject在转换中会报错，故返回Map

        JSONObject jsonObject = objectMapper.readValue(lines, JSONObject.class);

        boolean isSuccess = jsonObject.getBoolean("success");

        if (isSuccess) {

            JSONObject resultJson = jsonObject.getJSONObject("result");
            boolean completed = resultJson.getBoolean("completed");
            int type = resultJson.getInt("type");

            if (completed && type == 1) {
                logger.info("活动{}选择的数字内容已计算完成", activityId);

                JSONObject recommendations = resultJson.getJSONObject("recommendations");
                JSONArray dataList = recommendations.getJSONArray("data");

                for (int i = 0; i < dataList.size(); i++) {
                    JSONObject data = dataList.getJSONObject(i);

                    Map<Object, Object> resultMap = (Map<Object, Object>) map.get("result");
                    Map<Object, Object> recommendationsMap = (Map<Object, Object>) resultMap.get("recommendations");
                    List<Map<Object, Object>> datas = (List<Map<Object, Object>>) recommendationsMap.get("data");
                    datas.get(i).put("resultId", activityId);

                    String miguContentName = String.valueOf(data.get("name"));
                    if (StringUtils.isBlank(miguContentName) || "null".equalsIgnoreCase(miguContentName)) {
                        miguContentName = "结果集";
                        datas.get(i).put("name", miguContentName);
                    }

                    String miguContentType = String.valueOf(type);
                    String amount = String.valueOf(data.get("amount"));
                    String miguContentMarketing = String.valueOf(data.get("description"));
                    String miguContentShorturl = String.valueOf(data.get("shortUrl"));
                    String miguContentOutputpath = String.valueOf(data.get("outputPath"));

                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "resultId", activityId);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "name", miguContentName);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "type", miguContentType);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "amount", amount);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "description", miguContentMarketing);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "shortUrl", miguContentShorturl);
                    stringRedisTemplate.opsForHash().put("HASH-MIGU:" + activityId, "outputPath", miguContentOutputpath);

                    stringRedisTemplate.expire("HASH-MIGU:" + activityId, 24, TimeUnit.HOURS);
                }
            }
        }

        return map;
    }

    public void feedBack(HttpSession session, String activityId) throws IOException {
        Result result = this.getMiguContentConsoleToken(session);

        String token = null;

        if (JSONObject.fromObject(result.getData()).getBoolean("success")) {
            token = JSONObject.fromObject(result.getData()).getString("result");
        }

        String path = "http://" + miguContentConsoleHost + ":" + miguContentConsolePort + "/migu-content-console/activities/" + activityId + "/feedback?sid=" + token;

        String lines = this.doPost(path, "");

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject jsonObject = objectMapper.readValue(lines, JSONObject.class);

        boolean isSuccess = jsonObject.getBoolean("success");

        if (isSuccess) {
            logger.info("活动{}反馈成功！", activityId);
        } else {
            String msg = jsonObject.getString("message");
            logger.error("活动" + activityId + "反馈失败！" + msg);
        }

    }

    private String doPost(String path, String param) throws IOException {

        URL url = new URL(path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");

        urlConnection.connect();

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream())) {
            outputStreamWriter.write(param);
            outputStreamWriter.flush();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String lines = reader.readLine();
            return lines;
        }

    }

}
