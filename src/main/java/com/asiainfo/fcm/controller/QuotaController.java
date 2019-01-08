package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.City;
import com.asiainfo.fcm.entity.Quota;
import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.service.IBaseInitService;
import com.asiainfo.fcm.service.IQuotaService;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/28.
 */
@RestController
@RequestMapping("/quota")
public class QuotaController {

    @Autowired
    private IQuotaService quotaService;

    @Autowired
    private IBaseInitService baseInitService;

    /**
     *获取 渠道列表
     */
    @GetMapping("/getChannels")
    public Result getChannels() throws Exception{
        List<Map<String, Object>> channelList = quotaService.getChannels();
        return ResultUtil.success(channelList);
    }

    /**
     * 获取配额列表
     */
    @GetMapping("/getQuotaList")
    public Result getQuotaList(HttpSession session, @RequestParam("channelId") String channelId,@RequestParam("effectTime") String effectTime, @RequestParam("currentPage") long currentPage,@RequestParam("pageSize") long pageSize ) throws Exception{
        Map paramaterMap = new HashMap();
        paramaterMap.put("channelId",channelId);
        paramaterMap.put("effectTime",effectTime);
        paramaterMap.put("pageSize",pageSize);
        paramaterMap.put("startRow",(currentPage-1)*pageSize);

        User user = UserUtil.getCurrentUser(session);
        String userId = user.getUserId();
        String userRole = user.getUserRole();

        Map resultMap = new HashMap();
        List<Quota> quotaList = quotaService.getQuotaList(paramaterMap);
        int totalRecords = quotaService.getQuotaListTotalRecords(paramaterMap);

        resultMap.put("quotaList",quotaList);
        resultMap.put("totalRecords",totalRecords);
        resultMap.put("userRole",userRole);

        return ResultUtil.success(resultMap);

    }

    /**
     * 修改地市配额量
     */
    @PostMapping("/setCityQuota")
    public Result setCityQuota(@RequestBody Quota quotaMap,HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        int res = userRole.indexOf("quota");
        if ( res != -1 ){
            quotaService.setCityQuota(quotaMap);
        }else{
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }

        return ResultUtil.success();
    }

    /**
     * 新增地市
     *
     */
    @PostMapping("/addCityQuota")
    public Result addCityQuota(@RequestBody Quota quotaMap,HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String userRole = user.getUserRole();

        int res = userRole.indexOf("quota");
        if ( res != -1 ){
            //判断该渠道下该地市该月配额是否已存在
            Quota quota = quotaService.checkQuotaIsExist(quotaMap);
            if( quota == null ){
                quotaService.addCityQuota(quotaMap);
                return ResultUtil.success();
            }else {
                return ResultUtil.error(ResultEnum.QUOTA_EXIST);
            }

        }else{
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
    }

    /**
     * 获取未设置渠道配额的地市列表
     */
    @GetMapping("/getCityList")
    public Result getCityList(@RequestParam("channelId") String channelId) throws Exception{
        List<City> result = quotaService.getCityList(channelId);
        if( result.size() == 0 ){
            return  ResultUtil.error(ResultEnum.NO_CITY_NEED_QUOTA);
        }else{
            return ResultUtil.success(result);
        }

    }
}
