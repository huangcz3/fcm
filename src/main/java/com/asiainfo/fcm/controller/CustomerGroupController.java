package com.asiainfo.fcm.controller;

import com.asiainfo.ccc.entity.DatabaseConfig;
import com.asiainfo.ccc.entity.Table;
import com.asiainfo.ccc.motan.service.IMotanServiceAsync;
import com.asiainfo.ccc.motan.service.IMotanServiceSync;
import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IActivityService;
import com.asiainfo.fcm.service.ICustomerGroupService;
import com.asiainfo.fcm.task.AsyncTask;
import com.asiainfo.fcm.util.IDUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weibo.api.motan.rpc.FutureListener;
import com.weibo.api.motan.rpc.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户群管理.
 * Created by RUOK on 2017/6/16.
 */
@RestController
@RequestMapping("/customerGroups")
public class CustomerGroupController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerGroupController.class);

    @Autowired
    private ICustomerGroupService customerGroupService;

    @Autowired
    private IActivityService activityService;

    @Autowired
    private IMotanServiceSync motanServiceSync;

    @Value("${fcm.customer-group.tablename.prefix}")
    private String tableNamePreFix;

    @Value("${channel-suggest.mapping}")
    private String channelSuggestMapping;

    @Value("${fcm.channel.suggest.file.download.path}")
    private String channelSuggestdownLoadFilePath;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private IMotanServiceAsync motanServiceAsync;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Value("${fcm.customer-group.max-records}")
//    private long customerGroupMaxRecords;

    @GetMapping("/")
    public Result getCustomerGroups(HttpSession session, @RequestParam("createType") int createType, @RequestParam("currentPage") long currentPage, @RequestParam("pageSize") long pageSize) {
        User user = UserUtil.getCurrentUser(session);

        Map<String, Object> map = new HashMap<>();
        map.put("creatorId", user.getUserId());
        map.put("createType", createType);
//        map.put("customerGroupMaxRecords", customerGroupMaxRecords);

        long totalRecord = 0;

        if (createType == 1) {
            totalRecord = customerGroupService.getCocCustomerGroupsTotalRecord(map);
        } else if (createType > 1) {
            totalRecord = customerGroupService.getCustomerGroupsTotalRecord(map);
        }

        Page page = new Page(currentPage, pageSize, totalRecord);
        map.put("page", page);

        List<CustomerGroupInfo> customerGroupInfoList = null;

        if (createType == 1) {
            customerGroupInfoList = customerGroupService.getCocCustomerGroups(map);
        } else if (createType > 1) {
            customerGroupInfoList = customerGroupService.getCustomerGroups(map);
        }

        page.setDataList(customerGroupInfoList);

        return ResultUtil.success(page);
    }

    @GetMapping("/getDatabaseListByCity")
    public Result getDatabaseListByCity(HttpSession session) {
        User user = UserUtil.getCurrentUser(session);
        List<DatabaseConfig> databaseConfigList = motanServiceSync.getDatabaseListByCity(user.getCityId());
        return ResultUtil.success(databaseConfigList);
    }

    @PostMapping("/createMppCustomerGroup")
    public Result createMppCustomerGroup(HttpSession session, @RequestBody CustomerGroupInfo customerGroupInfo) throws SQLException {
        DatabaseConfig databaseConfig = motanServiceSync.getDatabaseById(customerGroupInfo.getMppDbName());

        Table table = new Table();
        table.setSchema(customerGroupInfo.getMppSchema());
        table.setTableName(customerGroupInfo.getMppTableName());
        table.setPhoneNoColumnName(customerGroupInfo.getMppPhoneColumn());
        table.setCustomizeFlag(customerGroupInfo.getCustomizeFlag());
        table.setMarketingColumnName(customerGroupInfo.getMppMarketingColumn());

        boolean tableFlag = motanServiceSync.verifyTable(databaseConfig, table);
        if (!tableFlag) {
            return ResultUtil.error(ResultEnum.MPP_TABLE_NOT_EXIST);
        }
        boolean phoneNoColumnFlag = motanServiceSync.verifyPhoneNoColumn(databaseConfig, table);
        if (!phoneNoColumnFlag) {
            return ResultUtil.error(ResultEnum.MPP_TABLE_PHONE_COLUMN_NOT_EXIST);
        }
        if (customerGroupInfo.getCustomizeFlag() == 1) {
            boolean marketingColumn = motanServiceSync.verifyMarketingColumn(databaseConfig, table);
            if (!marketingColumn) {
                return ResultUtil.error(ResultEnum.MPP_TABLE_MARKETING_COLUMN_NOT_EXIST);
            }
        }

        long amount = motanServiceSync.getCustomerAmount(databaseConfig, table);

        CustomerGroupInfo mppCustomerGroupInfo = new CustomerGroupInfo();
        String mppCustomerGroupId = IDUtil.generateCustomerGroupId();
        mppCustomerGroupInfo.setCustomerGroupId(mppCustomerGroupId);
        mppCustomerGroupInfo.setCustomerGroupName(customerGroupInfo.getCustomerGroupName());
        mppCustomerGroupInfo.setGroupTableName(tableNamePreFix + mppCustomerGroupId);

        User user = UserUtil.getCurrentUser(session);

        mppCustomerGroupInfo.setCreatorId(user.getUserId());
        mppCustomerGroupInfo.setCreatorName(user.getUserName());
        mppCustomerGroupInfo.setCreateType(2);
        mppCustomerGroupInfo.setAmount(amount);
        mppCustomerGroupInfo.setCustomizeFlag(customerGroupInfo.getCustomizeFlag());
        mppCustomerGroupInfo.setEffective(1);
        mppCustomerGroupInfo.setRemoveFlag(0);

        mppCustomerGroupInfo.setMppDbName(customerGroupInfo.getMppDbName());
        mppCustomerGroupInfo.setMppSchema(customerGroupInfo.getMppSchema());
        mppCustomerGroupInfo.setMppTableName(customerGroupInfo.getMppTableName());
        mppCustomerGroupInfo.setMppPhoneColumn(customerGroupInfo.getMppPhoneColumn());
        mppCustomerGroupInfo.setMppMarketingColumn(customerGroupInfo.getMppMarketingColumn());

        customerGroupService.createCustomerGroupTable(mppCustomerGroupInfo);

        customerGroupService.saveCustomerGroupInfo(mppCustomerGroupInfo);

        return ResultUtil.success(mppCustomerGroupInfo);
    }

    @GetMapping("/calculateCustomerGroup")
    public Result calculateCustomerGroup(@RequestParam("activityId") String activityId) {
        Activity activity = activityService.getActivityDetailInfo(activityId);

        User user = new User();
        user.setUserId(activity.getCreatorId());
        user.setUserName(activity.getCreatorName());

        List<String> customerGroupIdList = activity.getActivityCustomerGroupInfoList()
                .stream()
                .filter(a -> a.getCreateType() == 2)
                .filter(a -> customerGroupService.getCustomerGroup(a).getDataState() == 0)
                .map(ActivityCustomerGroupInfo::getCustomerGroupId).collect(Collectors.toList());

        if (customerGroupIdList.size() > 0) {
            ResponseFuture responseFuture = motanServiceAsync.pullCustomerAsync(activityId, customerGroupIdList);
            FutureListener futureListener = future -> {
                if (future.isSuccess()) {
                    asyncTask.calculateCustomerGroup(activity, user);
                } else {
                    logger.error("拉取活动：{}集市客户群异常！", activity.getActivityId());
                    logger.error("拉取集市客户群异常！", future.getException());
                }
            };
            responseFuture.addListener(futureListener);
        } else {
            asyncTask.calculateCustomerGroup(activity, user);
        }
        return ResultUtil.success();
    }


    /**
     * 获取渠道建议列表
     *
     * @param session
     * @param queryStr
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getChannelSuggestList")
    public Result getChannelSuggestList(HttpSession session, String queryStr, Integer currentPage, Integer pageSize) {
        // 取创建人
        User user = UserUtil.getCurrentUser(session);
        String createrId = user.getUserId();

        // 封装查询参数
        Map map = new HashMap();
        int startRow = (currentPage - 1) * pageSize;
        map.put("queryStr", queryStr);
        map.put("lengthRow", pageSize);
        map.put("startRow", startRow);
        map.put("createrId", createrId);

        // 封装分页返回结果
        List<Map> resultList = customerGroupService.getChannelSuggestListByCreateIdAndQueryStr(map);
        int allTotals = customerGroupService.getChannelSuggestListTotalsByCreateIdAndQueryStr(map);
        Map resultMap = new HashMap();
        resultMap.put("resultList", resultList);
        resultMap.put("allTotals", allTotals);

        // 返回
        return ResultUtil.success(resultMap);
    }

    /**
     * 渠道建议生成失败后重试接口
     *
     * @param customerGroupId
     * @return
     */
    @GetMapping("/reStartChannelSuggest")
    public Result reStartChannelSuggest(String customerGroupId) {
        // 根据id 取详细信息
        List<Map> maps = customerGroupService.getChannelSuggestById(customerGroupId);
        if (maps.size() > 0) {
            Map map = maps.get(0);

            // 更新状态为计算中
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("customerGroupId", customerGroupId);
            paramMap.put("status", 0);
            customerGroupService.updateChanelSuggestRecord(paramMap);

            // 取前一次失败后的表名，drop掉
            if (map.get("CUSTOMER_GROUP_TABLE_NAME") != null) {
                logger.info("渠道建议id {} 重试前, drop 零时表 {}", customerGroupId, map.get("CUSTOMER_GROUP_TABLE_NAME"));
                customerGroupService.dropTmpTable((String) map.get("CUSTOMER_GROUP_TABLE_NAME"));
            }

            // 调用计算接口
            asyncTask.calculateChannelSuggestCustomer((String) map.get("FILE_NAME"), customerGroupId, (String) map.get("CREATER_PHONE"), (String) map.get("ORIGINAL_FILE_NAME"));
            return ResultUtil.success();
        }
        return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    /**
     * 渠道建议获取下载详情
     *
     * @param customerGroupId
     * @return
     */
    @GetMapping("getChannelSuggestDownloadDetail")
    public Result getChannelSuggestDownloadDetail(String customerGroupId) throws IOException {
        // 创建返回结果对象
        Map<String, Object> resultMap = new HashMap<>();

        // 从redis中能取到数据直接返回
        String result = stringRedisTemplate.opsForValue().get("CHANNEL_SUGGEST:" + customerGroupId);
        if (result != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            return ResultUtil.success(objectMapper.readValue(result,Map.class));
        }
        // 根据id取详细信息
        List<Map> maps = customerGroupService.getChannelSuggestById(customerGroupId);
        if (maps.size() > 0) {
            Map map = maps.get(0);
            // 获取结果表名
            String tableName = (String) map.get("CUSTOMER_GROUP_TABLE_NAME");
            if (tableName != null) {
                // 根据结果表查出相关数据
                List<Map> list = customerGroupService.getChannelSuggestCountByTableName(tableName);
                if (list.size() > 0) {
                    // 封装前端渠道id和表中渠道id映射关系
                    String[] strings = channelSuggestMapping.split(",");
                    Map<String, String> channelSuggestMappingMap = new HashMap<>();
                    for (String s : strings) {
                        channelSuggestMappingMap.put(s.split("&")[0], s.split("&")[1]);
                    }

                    // 封装前端需要数据
                    long[] i = {0};
                    list.forEach(m -> {
                        String channelId = (String) m.get("CHANNEL");
                        i[0] = (i[0] + (int) m.get("COU"));
                        if (channelSuggestMappingMap.containsKey(channelId)) {
                            m.put("CSID", channelSuggestMappingMap.get(channelId));
                        }
                    });

                    // 封装返回结果
                    resultMap.put("channelList", list);
                    resultMap.put("total", i[0]);
                    ObjectMapper objectMapper = new ObjectMapper();
                    stringRedisTemplate.opsForValue().set("CHANNEL_SUGGEST:"+customerGroupId,objectMapper.writeValueAsString(resultMap));
                }
            }
        }

        return ResultUtil.success(resultMap);
    }

    /**
     * 下载文件
     *
     * @param response
     * @param customerGroupId
     * @param channel
     */
    @GetMapping("/downloadChannelSuggest")
    public void downloadChannelSuggest(HttpServletResponse response, String customerGroupId, String channel) {
        try {
            response.setHeader("content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

            // 根据id取详细信息
            List<Map> mapList = customerGroupService.getChannelSuggestById(customerGroupId);
            if (mapList.size() > 0) {
                Map map = mapList.get(0);

                // 封装前端渠道id和表中渠道id映射关系
                String[] strings = channelSuggestMapping.split(",");
                String[] mapping = {};
                for (String s : strings) {
                    if (s.contains(channel)) {
                        mapping = s.split("&");
                        break;
                    }
                }

                // 构造文件名
                String originalFileName = (String) map.get("ORIGINAL_FILE_NAME");
                String fileName = originalFileName.substring(0, originalFileName.length() - 4) + "_" + mapping[1] + ".txt";

                File file = new File(channelSuggestdownLoadFilePath + File.separator + customerGroupId + fileName);
                Path path = Paths.get(channelSuggestdownLoadFilePath, customerGroupId+fileName);


                logger.info("下载文件名：{}", fileName);
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);

                ServletOutputStream outputStream = response.getOutputStream();

                // 本地存在直接从本地取
                if (file.exists()) {
                    Files.copy(path,outputStream);
                } else {
                    // 得到结果表
                    String tableName = (String) map.get("CUSTOMER_GROUP_TABLE_NAME");
                    List<Map> reslut = customerGroupService.getChannelSuggestByChannel(tableName, mapping[0]);
                    // 写本地文件数据
                    PrintWriter pw = new PrintWriter(file);
                    for (Map m : reslut) {
                        pw.println(m.get("PHONE_NO").toString());
                    }
                    pw.close();

                    // 写回数据
                    for (Map m : reslut) {
                        outputStream.println(m.get("PHONE_NO").toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("下载文件异常");
        }
    }
}
