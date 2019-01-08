package com.asiainfo.fcm.task;

import com.asiainfo.ccc.motan.service.IMotanServiceAsync;
import com.asiainfo.fcm.controller.MiguContentConsoleController;
import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.model.ActivityChannelDetail;
import com.asiainfo.fcm.model.ActivityChannelRuleDetail;
import com.asiainfo.fcm.model.ActivityCustomerUpdateInfo;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.service.IFileService;
import com.asiainfo.fcm.service.IMentalMapService;
import com.asiainfo.fcm.util.IDUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weibo.api.motan.rpc.FutureListener;
import com.weibo.api.motan.rpc.ResponseFuture;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by RUOK on 2017/7/9.
 */
@Component
public class AsyncTask {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTask.class);

    @Autowired
    private IActivityService activityService;

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private IFileService fileService;

    @Value("${fcm.customer-group.tablename.prefix}")
    private String tableNamePreFix;

    @Value("${fcm.customer-group.tmp-tablename.prefix}")
    private String tmpTableNamePreFix;

    @Value("${fcm.customer-group.batch-insert.count}")
    private long batchInsertCount;

    @Value("${fcm.customer-group.max-records}")
    private long customerGroupMaxRecords;

    @Value("${fcm.file.upload.path}")
    private String uploadFilePath;

    @Value("${fcm.zt.ftp.host}")
    private String ztFtpHost;

    @Value("${fcm.zt.ftp.port}")
    private int ztFtpPort;

    @Value("${fcm.zt.ftp.username}")
    private String ztFtpUserName;

    @Value("${fcm.zt.ftp.password}")
    private String ztFtpPassword;

    @Value("${fcm.zt.ftp.upload.path}")
    private String ztFtpUploadPath;

    //发送短信接口
    @Value("${fcm.sms.send.url}")
    private String SEND_URL;

    @Value("${fcm.channel.suggest.file.download.path}")
    private String channelSuggestdownLoadFilePath;

    @Value("${channel-suggest.mapping}")
    private String channelSuggestMapping;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MiguContentConsoleController miguContentConsoleController;

    @Autowired
    private IMentalMapService mentalMapService;

    @Autowired
    private CustomerGroupUpdateTask customerGroupUpdateTask;

    @Autowired
    private MentalMapTask mentalMapTask;

    @Autowired
    private SmsAsyncTask smsAsyncTask;

    @Autowired
    private IMotanServiceAsync motanServiceAsync;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("fcm.toutiao.api.url")
    private String toutiaoApiUrl;

    public boolean processCustomerGroup(HttpSession session, Activity activity, User user) {

        CustomerRemoveInfo customerRemoveInfo = new CustomerRemoveInfo();
        customerRemoveInfo.setActivityId(activity.getActivityId());
        customerRemoveInfo.setActivityName(activity.getActivityName());
        customerRemoveInfo.setRemoveEmployee(activity.getRemoveEmployee());
        customerRemoveInfo.setRemoveRedList(activity.getRemoveRedList());
        customerRemoveInfo.setRemoveSensitive(activity.getRemoveSensitive());
        customerRemoveInfo.setRemoveCancel10086(activity.getRemoveCancel10086());
        customerRemoveInfo.setRemoveGroupUser(activity.getRemoveGroupUser());
        customerRemoveInfo.setRemoveUpload(activity.getRemoveUpload());
        customerRemoveInfo.setState(0);

        customerGroupService.saveCustomerRemoveInfo(customerRemoveInfo);

        boolean flag = true;
        boolean isMigu = true;
        boolean isCustomerInsight = true;
        List<String> customerGroupIdList = new ArrayList<>();

        List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList = activity.getActivityCustomerGroupInfoList();
        for (ActivityCustomerGroupInfo activityCustomerGroupInfo : activityCustomerGroupInfoList) {
            int createType = activityCustomerGroupInfo.getCreateType();

            if (createType == 2) {//数据集市
                CustomerGroupInfo mppCustomerGroupInfo = customerGroupService.getCustomerGroup(activityCustomerGroupInfo);
                if (mppCustomerGroupInfo.getDataState() == 0) {
                    customerGroupIdList.add(mppCustomerGroupInfo.getCustomerGroupId());
                }
            } else if (createType == 4) {//数字内容
                String customerGroupId = activityCustomerGroupInfo.getCustomerGroupId();

                long amount = 0;
                String miguContentId = activity.getActivityId();
                String miguContentName;
                int miguContentType;
                String miguContentMarketing = null;
                String miguContentShorturl = null;
                String miguContentOutputpath = null;

                if ("2".equals(customerGroupId)) {//用户推荐内容-标签
//                    miguContentId = activity.getActivityId();
                    miguContentName = activityCustomerGroupInfo.getCustomerGroupName();
                    miguContentType = Integer.valueOf(customerGroupId);
                } else if ("3".equals(customerGroupId)) {//用户推荐内容-上传
//                    miguContentId = activity.getActivityId();
                    miguContentName = activityCustomerGroupInfo.getCustomerGroupName();
                    miguContentType = Integer.valueOf(customerGroupId);
                } else {//内容推荐用户
                    Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("HASH-MIGU:" + customerGroupId);

                    amount = Long.valueOf(String.valueOf(map.get("amount")));
//                    miguContentId = String.valueOf(map.get("resultId"));
                    miguContentName = String.valueOf(map.get("name"));
                    miguContentType = Integer.valueOf(String.valueOf(map.get("type")));
                    miguContentMarketing = String.valueOf(map.get("description"));
                    miguContentShorturl = String.valueOf(map.get("shortUrl"));
                    miguContentOutputpath = String.valueOf(map.get("outputPath"));
                }

                CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();

                String generatedGroupId = IDUtil.generateCustomerGroupId();
                String groupTableName = tableNamePreFix + generatedGroupId;

                customerGroupInfo.setCustomerGroupId(generatedGroupId);
                customerGroupInfo.setCustomerGroupName(miguContentName);
                customerGroupInfo.setGroupTableName(groupTableName);
                customerGroupInfo.setCreatorId(user.getUserId());
                customerGroupInfo.setCreatorName(user.getUserName());
                customerGroupInfo.setCreateType(createType);
                customerGroupInfo.setAmount(amount);
                customerGroupInfo.setCustomizeFlag(0);
                customerGroupInfo.setEffective(1);
                customerGroupInfo.setRemoveFlag(0);
                customerGroupInfo.setMiguContentId(miguContentId);
                customerGroupInfo.setMiguContentName(miguContentName);
                customerGroupInfo.setMiguContentType(miguContentType);
                customerGroupInfo.setMiguContentMarketing(miguContentMarketing);
                customerGroupInfo.setMiguContentShorturl(miguContentShorturl);
                customerGroupInfo.setMiguContentCompleted(miguContentType == 1 ? 1 : 0);
                customerGroupInfo.setMiguContentOutputpath(miguContentOutputpath);

                customerGroupService.createCustomerGroupTable(customerGroupInfo);

                customerGroupService.saveCustomerGroupInfo(customerGroupInfo);

                flag = false;
            } else if (createType == 5) {//非实时智能地图
                activity.setActivityState(0);
                activityService.updateActivityState(activity);//更新活动状态为0，用户群生成中

                CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();

                String activityId = activityCustomerGroupInfo.getActivityId();//活动id
                String customerGroupId = IDUtil.generateCustomerGroupId();//生成的客户群id
                String groupTableName = tableNamePreFix + customerGroupId;//客户群表名

                customerGroupInfo.setCustomerGroupId(customerGroupId);
                customerGroupInfo.setCustomerGroupName(activityCustomerGroupInfo.getCustomerGroupName());
                customerGroupInfo.setGroupTableName(groupTableName);
                customerGroupInfo.setCreatorId(user.getUserId());
                customerGroupInfo.setCreatorName(user.getUserName());
                customerGroupInfo.setCreateType(createType);
                customerGroupInfo.setAmount(0);
                customerGroupInfo.setCustomizeFlag(0);
                customerGroupInfo.setEffective(1);
                customerGroupInfo.setRemoveFlag(0);

                customerGroupService.createCustomerGroupTable(customerGroupInfo);//创建客户群表，如FCM_C12345678912

                customerGroupService.saveCustomerGroupInfo(customerGroupInfo);//保存客户群信息

                customerGroupService.updateActCustomerGroupInfo(customerGroupId, activityId);//更新智能地图活动客户群信息表的客户群id

                //请求智能地图端的参数
                String customerGroupName = activityCustomerGroupInfo.getCustomerGroupName();
                String taskId = activityCustomerGroupInfo.getCustomerGroupId();
                String taskName = activity.getActivityName();
                String startTime = activity.getStartTime();
                String endTime = activity.getEndTime();

                mentalMapTask.mentalMapSaveActivity(activityId, taskId, taskName, startTime, endTime, customerGroupName);//异步方法 智能地图端 保存活动

                flag = false;
                isMigu = false;
            } else if (createType == 6) {
                isCustomerInsight = false;
            }

        }

        if (!flag && isMigu) {
            try {
                miguContentConsoleController.feedBack(session, activity.getActivityId());
            } catch (IOException e) {
                logger.error("反馈活动投放成功异常！", e);
            }
        }

        if (customerGroupIdList.size() > 0) {
            ResponseFuture responseFuture = motanServiceAsync.pullCustomerAsync(activity.getActivityId(), customerGroupIdList);
            FutureListener futureListener = future -> {
                if (future.isSuccess()) {
                    calculateCustomerGroup(activity, user);
                } else {
                    logger.error("拉取活动：{}集市客户群异常！", activity.getActivityId());
                    logger.error("拉取集市客户群异常！", future.getException());
                }
            };
            responseFuture.addListener(futureListener);
            flag = false;
        }

        return flag && isCustomerInsight;
    }

    @Async
    public void calculateCustomerGroup(Activity activity, User user) {

        CustomerRemoveInfo customerRemoveInfo = new CustomerRemoveInfo();
        customerRemoveInfo.setActivityId(activity.getActivityId());

        // 记录临时表名，后续删除用
        List<String> tmpGroupTableNameList = new ArrayList<>();

        try {
            //获取活动原始客户群列表
            List<ActivityCustomerGroupInfo> activityCustomerGroupInfoList = activity.getActivityCustomerGroupInfoList();

            List<CustomerGroupInfo> customerGroupInfoList = new ArrayList<>();

            for (ActivityCustomerGroupInfo activityCustomerGroupInfo : activityCustomerGroupInfoList) {

                CustomerGroupInfo customerGroupInfo;

                String customerGroupId = activityCustomerGroupInfo.getCustomerGroupId();
                if (activityCustomerGroupInfo.getCreateType() == 1 && !customerGroupId.startsWith(IDUtil.CUSTOMER_GROUP_ID_PREFIX)) { //标签库客户群时，同步数据

                    customerGroupInfo = this.syncCocCustomerGroup(customerGroupId, user);
                    customerGroupService.saveCustomerGroupInfo(customerGroupInfo);

                    customerGroupService.updateActivityCustomerGroupInfo(activityCustomerGroupInfo.getActivityId(), customerGroupInfo.getCustomerGroupId(), customerGroupInfo.getCocGroupId());

                } else {
                    customerGroupInfo = customerGroupService.getCustomerGroup(activityCustomerGroupInfo);
                }
                customerGroupInfoList.add(customerGroupInfo);
            }

            CustomerGroupInfo preRemoveCustomerGroupInfo;

            //获取剔除类型列表
            List<String> removeTypeList = this.getRemoveTypeList(activity);
            boolean tmpFlag = removeTypeList.size() == 0 ? false : true;

            logger.info("活动" + activity.getActivityId() + "包含客户群：{}", customerGroupInfoList.stream().map(CustomerGroupInfo::getCustomerGroupId).collect(Collectors.joining(",")));

            if (customerGroupInfoList.size() > 1) { //活动包含多个客户群时合并剔重
                preRemoveCustomerGroupInfo = this.unionCustomerGroups(customerGroupInfoList, tmpFlag, tmpGroupTableNameList);
            } else {
                preRemoveCustomerGroupInfo = customerGroupInfoList.get(0);
            }

            logger.info("剔除前客户群表名：{}", preRemoveCustomerGroupInfo.getGroupTableName());

            customerRemoveInfo.setOriginalAmount(preRemoveCustomerGroupInfo.getAmount());
            customerRemoveInfo.setCustomizeFlag(preRemoveCustomerGroupInfo.getCustomizeFlag());

            if (tmpFlag) {
                CustomerRemoveInfo finalCustomerRemoveInfo = this.removeCustomers(removeTypeList, activity, preRemoveCustomerGroupInfo, tmpGroupTableNameList, false);
                customerRemoveInfo.setRemoveEmployeeAmount(finalCustomerRemoveInfo.getRemoveEmployeeAmount());
                customerRemoveInfo.setRemoveRedListAmount(finalCustomerRemoveInfo.getRemoveRedListAmount());
                customerRemoveInfo.setRemoveSensitiveAmount(finalCustomerRemoveInfo.getRemoveSensitiveAmount());
                customerRemoveInfo.setRemoveCancel10086Amount(finalCustomerRemoveInfo.getRemoveCancel10086Amount());
                customerRemoveInfo.setRemoveGroupUserAmount(finalCustomerRemoveInfo.getRemoveGroupUserAmount());
                customerRemoveInfo.setRemoveUploadAmount(finalCustomerRemoveInfo.getRemoveUploadAmount());
                customerRemoveInfo.setFinalGroupTableName(finalCustomerRemoveInfo.getFinalGroupTableName());
                customerRemoveInfo.setFinalAmount(finalCustomerRemoveInfo.getFinalAmount());
            } else {
                customerRemoveInfo.setFinalGroupTableName(preRemoveCustomerGroupInfo.getGroupTableName());
                customerRemoveInfo.setFinalAmount(preRemoveCustomerGroupInfo.getAmount());
            }

            customerRemoveInfo.setState(1);

/*            if (!(activity.getActivityChannelDetailList().size() == 1 && "q03".equals(activity.getActivityChannelDetailList().get(0).getChannelId()))) {
                if (customerRemoveInfo.getFinalAmount() > customerGroupMaxRecords) {
                    customerRemoveInfo.setState(3);
                    customerRemoveInfo.setExceptionInfo("目标用户数超过最大限制");
                }
            }*/

            for (String tmpGroupTableName : tmpGroupTableNameList) { //删除临时表
                logger.info("删除临时表：{}", tmpGroupTableName);
                customerGroupService.dropTmpTable(tmpGroupTableName);
            }

        } catch (Exception e) {
            logger.error("活动" + activity.getActivityId() + "生成目标客户群异常", e);
            customerRemoveInfo.setState(2);
            customerRemoveInfo.setExceptionInfo(e.getMessage());
        } finally {
            customerGroupService.updateCustomerRemoveInfo(customerRemoveInfo);

            activity.setActivityState(customerRemoveInfo.getState());

            //智能地图目标用户由后台更新,只更新源目标用户生成状态
            if (activity.getSceneId() == 11 && activity.getActivityState() == 1) {
                mentalMapService.updateSourceCustomerBuildState(activity.getActivityId());
            }

            if(activity.getActivityState() == 1){
                activity.getActivityChannelDetailList()
                        .stream()
                        .filter(a -> a.getChannelId().equals("q11"))
                        .findFirst().ifPresent(b -> {
                            RestTemplate restTemplate = new RestTemplate();
                            String url = toutiaoApiUrl + "/tsa/add/" + activity.getActivityId();
                            restTemplate.getForObject(url, String.class);
                        }
                );
            }

            activityService.updateActivityState(activity);
            try {
                //触发审批短息发送
                activity.setFinalAmount(customerRemoveInfo.getFinalAmount());
                //发送审批短信
                smsAsyncTask.sendApprovalSms(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private CustomerGroupInfo syncCocCustomerGroup(String customerGroupId, User user) {
        Map<String, Object> cocMap = new HashMap<>();
        cocMap.put("cocCustomerGroupId", customerGroupId);
//        cocMap.put("customerGroupMaxRecords", customerGroupMaxRecords);

        CustomerGroupInfo customerGroupInfo = customerGroupService.getCocCustomerGroup(cocMap);

        String generatedGroupId = IDUtil.generateCustomerGroupId();
        String groupTableName = tableNamePreFix + generatedGroupId;
        customerGroupInfo.setCustomerGroupId(generatedGroupId);
        customerGroupInfo.setGroupTableName(groupTableName);
        customerGroupInfo.setCreatorName(user.getUserName());

        customerGroupService.createCustomerGroupTable(customerGroupInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("customerGroupInfo", customerGroupInfo);

        List<CustomerGroupDetail> customerGroupDetailList = customerGroupService.getCocCustomerGroupDetail(map);

        int count = 0;
        int endIndex;
        do {
            if (count + batchInsertCount > customerGroupDetailList.size()) {
                endIndex = customerGroupDetailList.size();
            } else {
                endIndex = Long.valueOf(count + batchInsertCount).intValue();
            }

            List<CustomerGroupDetail> subCustomerGroupDetailList = customerGroupDetailList.subList(count, endIndex);
            map.put("customerGroupDetailList", subCustomerGroupDetailList);

            customerGroupService.saveCustomerGroupDetail(map);

            count += batchInsertCount;
        } while (count < customerGroupDetailList.size());

        return customerGroupInfo;
    }

    private List<String> getRemoveTypeList(Activity activity) {

        int removeEmployee = activity.getRemoveEmployee();
        int removeRedList = activity.getRemoveRedList();
        int removeSensitive = activity.getRemoveSensitive();
        int removeCancel10086 = activity.getRemoveCancel10086();
        int removeGroupUser = activity.getRemoveGroupUser();
        int removeUpload = activity.getRemoveUpload();

        List<String> removeTypeList = new ArrayList<>();

        if (removeEmployee == 1) {
            removeTypeList.add("white");
        }

        if (removeRedList == 1) {
            removeTypeList.add("red");
        }

        if (removeSensitive == 1) {
            removeTypeList.add("black");
        }

        if (removeCancel10086 == 1) {
            removeTypeList.add("10086");
        }

        if(removeGroupUser == 1){
            removeTypeList.add("group");
        }

        if (removeUpload == 1) {
            removeTypeList.add("upload");
        }

        return removeTypeList;

    }

    private CustomerGroupInfo unionCustomerGroups(List<CustomerGroupInfo> customerGroupInfoList, boolean tmpFlag, List<String> tmpGroupTableNameList) {
        String unionGroupTableName;

        if (tmpFlag) {
            unionGroupTableName = tmpTableNamePreFix + IDUtil.generateCustomerGroupId();
            tmpGroupTableNameList.add(unionGroupTableName);
        } else {
            unionGroupTableName = tableNamePreFix + IDUtil.generateCustomerGroupId();
        }

        CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();
        customerGroupInfo.setGroupTableName(unionGroupTableName);

        //判断是否含有自定义客户群
        int flag = customerGroupInfoList.stream().filter(a -> a.getCustomizeFlag() == 1).count() > 0 ? 1 : 0;
        customerGroupInfo.setCustomizeFlag(flag);

        customerGroupService.createCustomerGroupTable(customerGroupInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("unionGroupTableName", unionGroupTableName);
        map.put("customizeFlag", flag);
        map.put("customerGroupInfoList", customerGroupInfoList);

        int count = customerGroupService.unionCustomerGroups(map);
        customerGroupInfo.setAmount(count);

        return customerGroupInfo;
    }

    private CustomerRemoveInfo removeCustomers(List<String> removeTypeList, Activity activity, CustomerGroupInfo customerGroupInfo, List<String> tmpGroupTableNameList, boolean cycleUpdateFlag) {

        String removeUploadCustomerGroupId = activity.getRemoveCustomerGroupId();

        CustomerRemoveInfo customerRemoveInfo = new CustomerRemoveInfo();

        long originalAmount = customerGroupInfo.getAmount();
        customerRemoveInfo.setOriginalAmount(originalAmount);

        boolean tmpFlag = true;

        for (int i = 0; i < removeTypeList.size(); i++) {
            String userType = removeTypeList.get(i);

            if (i == removeTypeList.size() - 1) {
                tmpFlag = false;
            }

            customerGroupInfo = this.removeCustomer(customerGroupInfo, userType, removeUploadCustomerGroupId, tmpFlag, tmpGroupTableNameList, cycleUpdateFlag);

            logger.info("表名------：{}", customerGroupInfo.getGroupTableName());

            long afterRemoveAmount = customerGroupInfo.getAmount();
            long removedAmount = originalAmount - afterRemoveAmount;

            switch (userType) {
                case "white":
                    customerRemoveInfo.setRemoveEmployeeAmount(removedAmount);
                    break;
                case "red":
                    customerRemoveInfo.setRemoveRedListAmount(removedAmount);
                    break;
                case "black":
                    customerRemoveInfo.setRemoveSensitiveAmount(removedAmount);
                    break;
                case "10086":
                    customerRemoveInfo.setRemoveCancel10086Amount(removedAmount);
                    break;
                case "group":
                    customerRemoveInfo.setRemoveGroupUserAmount(removedAmount);
                    break;
                case "upload":
                    customerRemoveInfo.setRemoveUploadAmount(removedAmount);
                    break;
                default:
                    break;
            }
            originalAmount = afterRemoveAmount;
        }

        customerRemoveInfo.setFinalGroupTableName(customerGroupInfo.getGroupTableName());
        customerRemoveInfo.setFinalAmount(originalAmount);
        return customerRemoveInfo;
    }

    private CustomerGroupInfo removeCustomer(CustomerGroupInfo customerGroupInfo, String userType, String removeUploadCustomerGroupId, boolean tmpFlag, List<String> tmpGroupTableNameList, boolean cycleUpdateFlag) {
        String groupTableName;
        if (tmpFlag) {
            groupTableName = tmpTableNamePreFix + IDUtil.generateCustomerGroupId();
            tmpGroupTableNameList.add(groupTableName);
        } else {
            groupTableName = tableNamePreFix + IDUtil.generateCustomerGroupId();
        }
        CustomerGroupInfo tmpCustomerGroupInfo = new CustomerGroupInfo();
        tmpCustomerGroupInfo.setGroupTableName(groupTableName);
        tmpCustomerGroupInfo.setCustomizeFlag(customerGroupInfo.getCustomizeFlag());

        customerGroupService.createCustomerGroupTable(tmpCustomerGroupInfo);

        Map<String, Object> map = new HashMap<>();

        if ("upload".equals(userType)) {
            ActivityCustomerGroupInfo activityCustomerGroupInfo = new ActivityCustomerGroupInfo();
            activityCustomerGroupInfo.setCustomerGroupId(removeUploadCustomerGroupId);
            activityCustomerGroupInfo.setCreateType(3);

            CustomerGroupInfo removeUploadCustomerGroupInfo = customerGroupService.getCustomerGroup(activityCustomerGroupInfo);
            String removeUploadCustomerGroupTableName = removeUploadCustomerGroupInfo.getGroupTableName();
            map.put("removeUploadCustomerGroupTableName", removeUploadCustomerGroupTableName);
        }

        map.put("userType", userType);
        map.put("validCustomerGroupInfo", tmpCustomerGroupInfo);
        map.put("customerGroupInfo", customerGroupInfo);
        map.put("cycleUpdateFlag", cycleUpdateFlag ? 1 : 0);

        int amount = customerGroupService.removeCustomers(map);

        tmpCustomerGroupInfo.setAmount(amount);

        return tmpCustomerGroupInfo;
    }

    @Async
    public void ftpImg(Activity activity) {

        List<ActivityChannelDetail> activityChannelDetailList = activity.getActivityChannelDetailList();

        activityChannelDetailList
                .stream()
                .filter(a -> "q03".equals(a.getChannelId()))
                .findFirst()
                .ifPresent(
                        b -> b.getActivityChannelRuleDetailList()
                                .stream()
                                .filter(c -> "R005".equals(c.getRuleId()))
                                .map(ActivityChannelRuleDetail::getRuleValue)
                                .forEach(d -> this.ftpImg(activity, d))
                );

        activityChannelDetailList
                .stream()
                .filter(a -> "q11".equals(a.getChannelId()))
                .findFirst()
                .ifPresent(
                        b -> b.getActivityChannelRuleDetailList()
                                .stream()
                                .filter(c -> "R005".equals(c.getRuleId()))
                                .map(ActivityChannelRuleDetail::getRuleValue)
                                .forEach(d -> {
                                    File image = new File(uploadFilePath + File.separator + d);
                                    redisTemplate.opsForValue().set("WECHATIMAGE:" + activity.getActivityId(), image);
                                })
                );
    }

    private void ftpImg(Activity activity, String imgName) {

        logger.info("上传图片：{}", imgName);

        File img = new File(uploadFilePath + File.separator + imgName);

        try {

//            FTPUtil.uploadFile(ztFtpHost, ztFtpPort, ztFtpUserName, ztFtpPassword, ztFtpUploadPath, imgName, inputStream);

            Map<String, Object> imgRelMap = new HashMap<>();
            imgRelMap.put("activityId", activity.getActivityId());
            imgRelMap.put("imgName", imgName);

            fileService.addActivityImgRel(imgRelMap);

            Map<String, Object> imgMap = new HashMap<>();
            imgMap.put("activityId", activity.getActivityId());
            imgMap.put("imgName", imgName);
            imgMap.put("ztImg", Files.readAllBytes(img.toPath()));
            imgMap.put("imgName", imgName);

            fileService.addActivityImg(imgMap);

        } catch (IOException e) {
            logger.error("上传图片：" + imgName + "异常", e);
        }

    }

    /**
     * 日更新标签库客户群数据
     */
    public void updateActivityCustomer() {
        List<ActivityCustomerUpdateInfo> activityCustomerUpdateInfoList = customerGroupService.getCustomerGroupForUpdateByCycle();

        for (ActivityCustomerUpdateInfo activityCustomerUpdateInfo : activityCustomerUpdateInfoList) {

            try {

                List<CustomerGroupInfo> syncCocCustomerGroupInfoList = new ArrayList<>();

                for (CustomerGroupInfo customerGroupInfo : activityCustomerUpdateInfo.getCustomerGroupInfoList()) {
                    Map<String, Object> cocMap = new HashMap<>();

                    cocMap.put("cocCustomerGroupId", customerGroupInfo.getCocGroupId());
//                    cocMap.put("customerGroupMaxRecords", customerGroupMaxRecords);

                    CustomerGroupInfo cocCustomerGroupInfo = customerGroupService.getCocCustomerGroup(cocMap);


                    if (cocCustomerGroupInfo == null) {
                        continue;
                    }

                    String curDataTime = customerGroupInfo.getDataTime();
                    String cocDataTime = cocCustomerGroupInfo.getDataTime();

                    if (cocDataTime.compareTo(curDataTime) > 0) {//有更新数据

                        logger.info("活动{}有更新数据！", activityCustomerUpdateInfo.getActivityId());

                        User user = new User();
                        user.setUserId(activityCustomerUpdateInfo.getCreatorId());
                        user.setUserName(activityCustomerUpdateInfo.getCreatorName());

                        CustomerGroupInfo syncCocCustomerGroupInfo = this.syncCocCustomerGroup(customerGroupInfo.getCocGroupId(), user);

                        logger.info("同步后客户群id：{}", syncCocCustomerGroupInfo.getCustomerGroupId());

                        customerGroupService.updateCocGroupDateTime(customerGroupInfo.getCustomerGroupId(), cocDataTime);

                        syncCocCustomerGroupInfoList.add(syncCocCustomerGroupInfo);

                    }
                }

                List<String> tmpGroupTableNameList = new ArrayList<>();
                CustomerGroupInfo customerGroupInfo;

                if (syncCocCustomerGroupInfoList.size() == 0) {
                    continue;
                } else if (syncCocCustomerGroupInfoList.size() > 1) {
                    customerGroupInfo = this.unionCustomerGroups(syncCocCustomerGroupInfoList, true, tmpGroupTableNameList);
                } else {
                    customerGroupInfo = syncCocCustomerGroupInfoList.get(0);
                }

                Activity activity = new Activity();

                activity.setRemoveEmployee(activityCustomerUpdateInfo.getRemoveEmployee());
                activity.setRemoveRedList(activityCustomerUpdateInfo.getRemoveRedList());
                activity.setRemoveSensitive(activityCustomerUpdateInfo.getRemoveSensitive());
                activity.setRemoveCancel10086(activityCustomerUpdateInfo.getRemoveCancel10086());
                activity.setRemoveGroupUser(activityCustomerUpdateInfo.getRemoveGroupUser());
                activity.setRemoveUpload(activityCustomerUpdateInfo.getRemoveUpload());
                activity.setRemoveCustomerGroupId(activityCustomerUpdateInfo.getRemoveCustomerGroupId());

                List<String> removeTypeList = this.getRemoveTypeList(activity);

                boolean tmpFlag = removeTypeList.size() == 0 ? false : true;

                String diffCustomerGroupId = IDUtil.generateCustomerGroupId();
                String groupTableName;
                if (tmpFlag) {
                    groupTableName = tmpTableNamePreFix + IDUtil.generateCustomerGroupId();
                    tmpGroupTableNameList.add(groupTableName);
                } else {
                    groupTableName = tableNamePreFix + IDUtil.generateCustomerGroupId();
                }

                CustomerGroupInfo diffCustomerGroupInfo = new CustomerGroupInfo();
                diffCustomerGroupInfo.setCustomerGroupId(diffCustomerGroupId);
                diffCustomerGroupInfo.setGroupTableName(groupTableName);
                diffCustomerGroupInfo.setCustomizeFlag(activityCustomerUpdateInfo.getCustomizeFlag());

                customerGroupService.createCustomerGroupTable(diffCustomerGroupInfo);

                String preFinalGroupTableName = activityCustomerUpdateInfo.getFinalGroupTableName();

                Map<String, Object> map = new HashMap<>();
                map.put("diffCustomerGroupInfo", diffCustomerGroupInfo);
                map.put("newGroupTableName", customerGroupInfo.getGroupTableName());
                map.put("preFinalGroupTableName", preFinalGroupTableName);

                int amount = customerGroupService.diffCustomerGroup(map);
                diffCustomerGroupInfo.setAmount(amount);

                CustomerRemoveInfo customerRemoveInfo = this.removeCustomers(removeTypeList, activity, diffCustomerGroupInfo, tmpGroupTableNameList, true);

                customerRemoveInfo.setActivityId(activityCustomerUpdateInfo.getActivityId());

                customerGroupService.cycleUpdateCustomerRemoveInfo(customerRemoveInfo);

                tmpGroupTableNameList.add(preFinalGroupTableName);

                tmpGroupTableNameList.stream().forEach(a -> customerGroupService.dropTmpTable(a));

                // 用户群日更新到redis
                customerGroupUpdateTask.updateCustomerGroups(customerRemoveInfo.getActivityId(), customerRemoveInfo.getFinalGroupTableName());
            } catch (Exception e) {
                logger.error("更新活动" + activityCustomerUpdateInfo.getActivityId() + "标签库客户群异常", e);
            }
        }
    }

    @Async
    public void toRedisOfJson(Activity activity) {
        Activity activityTmp = activity;
//        activityTmp.getActivityChannelDetailList().stream().forEach(activityChannelDetail -> {
//            activityChannelDetail.setApproverInfoList(null);
//        });
        JSONObject json = JSONObject.fromObject(activity);
        stringRedisTemplate.opsForValue().set("ACTIVE-BASEINFO:" + activity.getActivityId(), json.toString());

    }

    @Async
    public void calculateChannelSuggestCustomer(String filename, String customerGroupId, String phone, String originalFileName) {
        BufferedReader br = null;
        FileReader fr = null;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("customerGroupId", customerGroupId);
        try {
            // 号码取到Set中（去重），转List（入表）
            File file = new File(uploadFilePath + File.separator + filename);
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            Set<String> set = new HashSet<>();
            String phoneNo = null;
            long count = 0;
            while ((phoneNo = br.readLine()) != null) {
                count++;
                if (isPhoneNo(phoneNo)) {
                    set.add(phoneNo);
                }
            }

            long effectiveCount = set.size();
            paramMap.put("allAmcount", count + "");
            paramMap.put("effectiveAmcount", effectiveCount + "");
            if (effectiveCount == 0) {
                paramMap.put("status", -1);
                return;
            }

            List<CustomerGroupDetail> phoneList = new ArrayList<>();
            set.forEach(s -> {
                CustomerGroupDetail detail = new CustomerGroupDetail();
                detail.setPhoneNo(s);
                phoneList.add(detail);
            });

            // 建表，入表
            String tmpGroupTableName = tmpTableNamePreFix + IDUtil.generateCustomerGroupId();
            CustomerGroupInfo tmpCustomerGroupInfo = new CustomerGroupInfo();
            tmpCustomerGroupInfo.setGroupTableName(tmpGroupTableName);
            customerGroupService.createCustomerGroupTable(tmpCustomerGroupInfo);
            paramMap.put("customerGroupTableName", tmpGroupTableName);
            logger.info("渠道建议id  {} 建立零时表 {}", customerGroupId, tmpGroupTableName);

            int i = 0;
            do {
                int len = ((i + 1) * 500 > phoneList.size()) ? phoneList.size() : (i + 1) * 500;
                Map<String, Object> map = new HashMap<>();
                map.put("customerGroupInfo", tmpCustomerGroupInfo);
                map.put("customerGroupDetailList", phoneList.subList(i * 500, len));
                customerGroupService.saveCustomerGroupDetail(map);
                i++;
            } while (i * 500 < phoneList.size());

            // 建表，入表
            String groupTableName = tableNamePreFix + IDUtil.generateCustomerGroupId();
            CustomerGroupInfo customerGroupInfo = new CustomerGroupInfo();
            customerGroupInfo.setGroupTableName(groupTableName);
            customerGroupInfo.setCustomizeFlag(1);
            customerGroupService.createCustomerGroupTable(customerGroupInfo);
            paramMap.put("customerGroupTableName", groupTableName);
            logger.info("渠道建议id  {} 建立结果表 {}", customerGroupId, groupTableName);

            Map map = new HashMap();
            map.put("customerGroupTableName", groupTableName);
            map.put("tmpCustomerGroupTableName", tmpGroupTableName);
            customerGroupService.calculateChannelSuggestCustomer(map);
            customerGroupService.dropTmpTable(tmpGroupTableName);
            logger.info("渠道建议id  {} 删除零时表 {}", customerGroupId, tmpGroupTableName);
            paramMap.put("status", 2);

            try {
                // 封装前端渠道id和表中渠道id映射关系
                String[] strings = channelSuggestMapping.split(",");
                for (String s : strings) {
                    String[] strings1 = s.split("&");
                    String fname = customerGroupId + originalFileName.substring(0, originalFileName.length() - 4) + "_" + strings1[1] + ".txt";
                    List<Map> maps = customerGroupService.getChannelSuggestByChannel(groupTableName, strings1[0]);

                    if (maps.size() > 0) {
                        PrintWriter printWriter = new PrintWriter(channelSuggestdownLoadFilePath + File.separator + fname);
                        // 写数据
                        for (Map m : maps) {
                            printWriter.println(m.get("PHONE_NO").toString());
                        }
                        printWriter.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("渠道建议 {} 生成本地文件失败", customerGroupId);
            }

            try {
                Map resultMap = new HashMap();
                // 根据结果表查出相关数据
                List<Map> list = customerGroupService.getChannelSuggestCountByTableName(groupTableName);
                if (list.size() > 0) {
                    // 封装前端渠道id和表中渠道id映射关系
                    String[] strings = channelSuggestMapping.split(",");
                    Map<String, String> channelSuggestMappingMap = new HashMap<>();
                    for (String s : strings) {
                        channelSuggestMappingMap.put(s.split("&")[0], s.split("&")[1]);
                    }
                    // 封装前端需要数据
                    long[] l = {0};
                    list.forEach(m -> {
                        String channelId = (String) m.get("CHANNEL");
                        l[0] = (l[0] + (int) m.get("COU"));
                        if (channelSuggestMappingMap.containsKey(channelId)) {
                            m.put("CSID", channelSuggestMappingMap.get(channelId));
                        }
                    });
                    // 封装redis中的缓存数据
                    resultMap.put("channelList", list);
                    resultMap.put("total", l[0]);
                    ObjectMapper objectMapper = new ObjectMapper();
                    stringRedisTemplate.opsForValue().set("CHANNEL_SUGGEST:" + customerGroupId, objectMapper.writeValueAsString(resultMap));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("渠道建议 {} 信息入redis失败", customerGroupId);
            }

            String msg = "您上传的 " + originalFileName + " 文件已在后台计算完成，总行数:" + count + "，有效用户数:" + effectiveCount;

            smsAsyncTask.smsHandler(SEND_URL, "", phoneNo, msg);
            logger.info("通知短信发送成功, 号码{},  内容 {}", phone, msg);

        } catch (Exception e) {
            e.printStackTrace();
            paramMap.put("status", 1);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            paramMap.put("completeTime", new Date());
            customerGroupService.updateChanelSuggestRecord(paramMap);
        }
    }

    private static boolean isPhoneNo(String phoneNo) {
        String regExp = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(phoneNo);
        return matcher.matches();
    }

}
