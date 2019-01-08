package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.task.AsyncTask;
import com.asiainfo.fcm.util.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by LiuJH on 2018/3/7.
 */
@RestController
@RequestMapping("/customerInsight")
public class CustomerInsightController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerInsightController.class);
    @Autowired
    private IActivityService activityService;
    @Autowired
    private FileController fileController;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private ICustomerGroupService customerGroupService;

    @Value("${customer-insight.host}")
    private String customerInsightHost;
    @Value("${customer-insight.port}")
    private String customerInsightPort;
    @Value("${customer-insight.download.host}")
    private String customerInsightDownloadHost;
    @Value("${customer-insight.download.port}")
    private int customerInsightDownloadPort;
    @Value("${customer-insight.download.user}")
    private String customerInsightDownloadUser;
    @Value("${customer-insight.download.pwd}")
    private String customerInsightDownloadPwd;
    @Value("${customer-insight.localPath}")
    private String customerInsightLocalPath;
    @Value("${customer-insight.filepath}")
    private String customerInsightFilePath;

    @GetMapping("/loginUrl")
    public Result getCustomerInsightLoginUrl(HttpSession session) throws Exception {
        User user = UserUtil.getCurrentUser(session);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("loginNo=");
        stringBuilder.append(user.getUserId());
        stringBuilder.append("&pwd=");
        stringBuilder.append(user.getPassword());
        stringBuilder.append("&op_time=");
        stringBuilder.append(new Timestamp(System.currentTimeMillis()).toString());
        String param = PasswordUtil.encrypt(stringBuilder.toString());

        return ResultUtil.success("http://" + customerInsightHost + ":" + customerInsightPort + "/pm_insight/build/throughLogin?PN=" + param);
    }

    @GetMapping("/pageResults")
    public Map pageResults(HttpSession session, @RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize) throws IOException {

        // 取登录用户信息
        User user = UserUtil.getCurrentUser(session);

        // everyPage 每页条数 currentPage 当前页数  subAccount 经分帐号
        Map map = new HashMap();
        map.put("everyPage", pageSize + "");
        map.put("currentPage", currentPage + "");
        map.put("subAccount", user.getUserId());

        // 发送请求获取数据
        String lines = HttpUtil.sendGet("http://" + customerInsightHost + ":" + customerInsightPort + "/pm_insight/build/getUserGroup", map);
        // 转json，封装数据
        JSONObject jsonObject = JSONObject.fromObject(lines);
        map.clear();
        map.put("code", "-1");
        map.put("msg", "未知错误");
        if ("001".equals(jsonObject.get("resultCode"))) {
            map.put("code", "0");
            map.put("msg", "成功");
            Map resultContent = (Map) jsonObject.get("resultContent");
            map.put("count", resultContent.get("totalPageNum"));
            map.put("data", resultContent.get("data"));
        }

        return map;
    }

    @Async
    public void getDetailInfo(String activityId) {

        logger.info("开始查询 {} 用户群是否生成成功",activityId);

        Map<String,String> map = new HashMap<>();
        map.put("activityId",activityId);

        String lines = HttpUtil.sendGet("http://" + customerInsightHost + ":" + customerInsightPort + "/pm_insight/build/getFileState", map);
        System.out.println(lines);

        // 校验数据是否生成完成
        String status = (String) JSONObject.fromObject(lines).get("resultContent");
        if (status.equals("0")) {
            logger.info(" {} 用户群生成中",activityId);
            return;
        }

        try {
            if ("1".equals(status)) {
                // 取活动信息
                Activity activity = activityService.getActivityDetailInfo(activityId);

                // 获取活动用户群列表
                List<ActivityCustomerGroupInfo> customerGroupInfoList = activity.getActivityCustomerGroupInfoList();
                List<ActivityCustomerGroupInfo> groupInfoList = customerGroupInfoList.stream().filter(c -> c.getCreateType() == 6 && c.getCustomerGroupId().length() != 14 && !c.getCustomerGroupId().startsWith("C")).collect(Collectors.toList());

                // 生成文件名
                String filename = activityId + ".txt";

                if (groupInfoList.size() == 1 && FTPUtil.downloadFilebyFilename(customerInsightDownloadHost, customerInsightDownloadPort, customerInsightDownloadUser, customerInsightDownloadPwd, customerInsightFilePath, filename, customerInsightLocalPath, filename)) {
                    // 取活动用户群
                    ActivityCustomerGroupInfo groupInfo = groupInfoList.get(0);

                    // 封装上传电话号码到表中所需参数
                    File file = new File(customerInsightLocalPath + filename);
                    String fileName = file.getName();
                    UploadFile uploadFile = new UploadFile();
                    uploadFile.setFileName(fileName);
                    uploadFile.setOriginalFileName(groupInfo.getCustomerGroupName() + ".");
                    uploadFile.setUploaderId(activity.getCreatorId());
                    uploadFile.setUploaderName(activity.getCreatorName());
                    uploadFile.setUseType(1);

                    // 调用方法上传文件，得到返回结果
                    Result result = fileController.createCustomerGroup(uploadFile, file);
                    if (!(0 == result.getCode())) {
                        throw new Exception("上传用户群文件异常");
                    }
                    CustomerGroupInfo customerGroupInfo = (CustomerGroupInfo) result.getData();

                    // 更改活动用户群id
                    customerGroupService.updateActivityCustomerGroupInfo(activityId, customerGroupInfo.getCustomerGroupId(), groupInfo.getCustomerGroupId());
                    groupInfo.setCustomerGroupId(customerGroupInfo.getCustomerGroupId());

                    // 调用计算用户群方法
                    User user = new User();
                    user.setUserId(activity.getCreatorId());
                    user.setUserName(activity.getCreatorName());
                    asyncTask.calculateCustomerGroup(activity, user);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("客户洞察处理用户群异常", e);
        }
        Activity activity = new Activity();
        activity.setActivityId(activityId);
        activity.setActivityState(2);
        activityService.updateActivityState(activity);
    }
}
