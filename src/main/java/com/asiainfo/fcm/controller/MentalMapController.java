package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.service.IMentalMapService;
import com.asiainfo.fcm.util.HttpUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PuMg on 2017/8/31/0031.
 * 智能地图
 */
@RestController
@RequestMapping("/mental")
public class MentalMapController {

    private static final Logger logger = LoggerFactory.getLogger(MentalMapController.class);

    @Autowired
    private IMentalMapService mentalMapService;

    @Value("${fcm.mentalMap.url}")
    private String mentalMapUrl;

    /**
     *  启动智能地图目标用户计算
     * @param activityId
     * @param userFilePath
     * @return
     * @throws Exception
     */
    @GetMapping("/saveMentalMapAct")
    public Result saveMentalMaoAct(@RequestParam String activityId,
                                   @RequestParam(value = "userFilePath",required = false) String userFilePath)throws Exception{
       Map paramMap = new HashMap<>();
       paramMap.put("activityId",activityId);
//       paramMap.put("userFilePath",userFilePath);

       Map resultMap =  mentalMapService.saveMentalMapActivityInfo(paramMap);
        logger.info("活动: -> {} 保存成功！",activityId);

       return ResultUtil.success(resultMap);
    }


    /**
     * 查询智能地图选择结果
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMarkedInfo")
    public Result getMarkedInfo(@RequestParam String activityId)throws Exception{
        Map paramMap = new HashMap<>();
        paramMap.put("taskId",activityId);
        String mapUrl = mentalMapUrl+"/activity/info";
        HttpUtil doGet = new HttpUtil();
        String lines = doGet.sendGet(mapUrl,paramMap);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);

        return ResultUtil.success(resultMap);
    }

    /**
     * 用户群是否计算完成
     * @param activityId
     * @return
     * @throws Exception
     */
    @GetMapping("/isComplete")
    public int isComplete(@RequestParam String activityId) throws Exception{
        //拼接参数
        Map paramMap = new HashMap<>();
        paramMap.put("taskId", activityId);

        String mapUrl = mentalMapUrl + "/activity/isComplete";
        HttpUtil doGet = new HttpUtil();
        String lines = doGet.sendGet(mapUrl,paramMap);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Object, Object> resultMap = objectMapper.readValue(lines, Map.class);
        Map data = (Map) resultMap.get("data");
        int resultCode = (int) data.get("isComplete");
        return resultCode;
    }


}
