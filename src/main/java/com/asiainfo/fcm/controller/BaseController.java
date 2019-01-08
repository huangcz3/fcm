package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.model.AdPosition;
import com.asiainfo.fcm.model.IopInfo;
import com.asiainfo.fcm.service.IBaseInitService;
import com.asiainfo.fcm.service.IUserService;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author MgPu
 * 获取页面下拉选项
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private IBaseInitService baseService;

    @Value("${fcm.otherBrowser.url}")
    private String otherBrowserUrl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IUserService userService;

    @GetMapping("/getUserInfo")
    public Result getUserInfo(HttpSession session)throws Exception{
        try{
            User user = UserUtil.getCurrentUser(session);
            Map map = new HashMap<>();
            map.put("userId",user.getUserId());
            map.put("userName",user.getUserName());
            map.put("cityId",user.getCity_code());
            return ResultUtil.success(map);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取场景选项
     * @return sceneList (场景list)
     * @throws Exception
     */
    @RequestMapping("/getScene")
    @ResponseBody
    public Result getScene(HttpServletRequest request)throws Exception{
        String cityId = request.getParameter("cityId");
        List<SceneInfo> sceneList = baseService.getSceneInit(cityId);
        return ResultUtil.success(sceneList);
    }

    /**
     *  获取可执行渠道
     * @param session
     * @param cityId
     * @return
     * @throws Exception
     */
    @GetMapping("/getChannel")
    public Result getChannel(HttpSession session, @RequestParam(value = "cityId", defaultValue = "0") String cityId)throws Exception{
        if("0".equals(cityId)){
            User user = UserUtil.getCurrentUser(session);
            cityId = user.getCityId();
        }
        List<ChannelInfo> channelInfoList = baseService.getChanelInitByCityId(cityId);
        return ResultUtil.success(channelInfoList);
    }

    /**
     * 获取统计口径中 推荐业务
     */
    @GetMapping("/getRecommendProductInfo")
    public Result getRecommendProductInfo(HttpServletRequest request)throws Exception{
        Map resultMap = new HashMap<>();
        String prcType = request.getParameter("prcType");
        String nowPageNum = request.getParameter("nowPageNum");
        int lengthRow = Integer.valueOf(request.getParameter("lengthRow"));
        String queryStr = request.getParameter("queryStr");
        User  user = (User) request.getSession().getAttribute("user");
        String cityId = user.getCityId();
        int startRow = (Integer.valueOf(nowPageNum) - 1) * lengthRow + 1;

        List<Map<String, Object>> reconmondList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("prcType", prcType);
        map.put("start",startRow);
        map.put("end", Integer.valueOf(nowPageNum)*lengthRow);
        map.put("queryStr", queryStr);
        map.put("cityId", cityId);
        int allTotals = 0;
        if ("2".equals(prcType)) {
            //更改营销活动检索代码模糊查询
            if(queryStr.length()<=4 || queryStr.isEmpty()){
                List<Map<String, Object>> rL = baseService.getReconmondList(map);
                allTotals = baseService.getReconmondListAllTotal(map);
                for (Map<String, Object> m : rL) {
                    map.put("prcId", m.get("PRC_ID"));
                    m.put("CHILD", baseService.getReconmondSecondList(map));
                    reconmondList.add(m);
                }
            }else{
                String tmpStr = queryStr.substring(0,4);
                map.put("queryStr",tmpStr);
                List<Map<String, Object>> rL = baseService.getReconmondList(map);
                allTotals = baseService.getReconmondListAllTotal(map);
                map.put("queryStr",queryStr);
                rL.stream().forEach(m -> {
                    map.put("prcId", m.get("PRC_ID"));
                    m.put("CHILD", baseService.getReconmondSecondList(map));
                    reconmondList.add(m);
                });
            }
        } else {
            List<Map<String, Object>> rL = baseService.getReconmondList(map);
            allTotals = baseService.getReconmondListAllTotal(map);
            reconmondList.addAll(rL);
        }
        resultMap.put("businessList",reconmondList);
        resultMap.put("allTotals",allTotals);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取地市和渠道下拉
     */
    @GetMapping("/getDropInfo")
    public Result getDropInfo(HttpSession session)throws Exception{
        Map resultMap = new HashMap<>();
        User user = UserUtil.getCurrentUser(session);
        List<City> cityList = baseService.getCityList(user.getCityId());
        List<ChannelInfo> channelInfoList = baseService.getChanelInitByCityId(user.getCityId());
        resultMap.put("cityList",cityList);
        resultMap.put("channelInfoList",channelInfoList);
        resultMap.put("currentCity",user.getCityId());
        resultMap.put("deptId",user.getDeptId());
        resultMap.put("deptName",user.getDeptName());
        resultMap.put("currentCityName",user.getCityName());

        return ResultUtil.success(resultMap);
    }

    /**
     * 获取掌厅下拉选项
     */
    @GetMapping("getZTDropInfo")
    public Result getZTDropInfo()throws Exception{
        Map resultMap = new HashMap<>();
        List<AdPosition> adPositionList = baseService.getAdPosition();
        List<Map> mapList = baseService.getLinkCode();
        resultMap.put("adPositionList",adPositionList);
        resultMap.put("codeList",mapList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取互联网渠道下拉
     */
    @GetMapping("getIopDropInfo")
    public Result getIopDropInfo()throws Exception{
        Map resultMap = new HashMap<>();
        List<IopInfo> iopInfoList = baseService.getIopDropInfo();
        resultMap.put("iopList",iopInfoList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取缴费渠道选项
     */
    @GetMapping("/getPayType")
    public Result getPayType()throws Exception{
        Map resultMap = new HashMap<>();
        List<Map> mapList = baseService.getPayType();
        resultMap.put("payTypeList",mapList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 获取追尾短信提醒类型
     */
    @GetMapping("/getRearEndType")
    public Result getRearEndType()throws Exception{
        Map resultMap = new HashMap<>();
        List<Map> mapList = baseService.getRearEndType();
        resultMap.put("rearEndType",mapList);
        return ResultUtil.success(resultMap);
    }


    /**
     * 获取上网行为 APP 分类
     */
    @GetMapping("/getAppType")
    public Result getAppType()throws Exception{
        Map resultMap = new HashMap<>();
        List<AppTypeDefine> typeDefineList = baseService.getAppTypeInfo();
        resultMap.put("appTypeList",typeDefineList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 根据app类型获取app信息
     */
    @GetMapping("/getAppInfoList")
    public Result getAppInfoList(@RequestParam String typeId, @RequestParam String searchVal)throws Exception{
        Map paramMap = new HashMap();
        Map resultMap = new HashMap<>();

        paramMap.put("typeId",typeId);
        paramMap.put("searchVal",searchVal);
        List<Map> mapList = baseService.getAppInfoList(paramMap);
        resultMap.put("appInfoList",mapList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 根据所选场景 获取可执行渠道
     */
    @GetMapping("getChannelsBySceneId")
    public Result getChannelsBySceneId(HttpSession session,@RequestParam String sceneId)throws Exception{
        Map resultMap = new HashMap<>();
        Map parameterMap = new HashMap<>();
        User user = UserUtil.getCurrentUser(session);
        parameterMap.put("sceneId",sceneId);
        parameterMap.put("cityId",user.getCityId());
        List<ChannelInfo> channelInfoList = baseService.getChanelsBySceneId(parameterMap);
        resultMap.put("channelInfo",channelInfoList);
        return ResultUtil.success(resultMap);
    }

    /**
     * @description: 获取效果评估中 地市和渠道下拉列表
     * @author Mgpu
     * @throws Exception
     */
    @GetMapping("/getEvaCityInit")
    public Result getEvaCityInit()throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<Map> cityMap = baseService.getEvaCityList();
        List<Map> channelMap = baseService.getChannelList();
        List<String> allLevel = baseService.getAllLevel();

        resultMap.put("cityOption", cityMap);
        resultMap.put("channelOption", channelMap);
        resultMap.put("allLevel",allLevel);
        return ResultUtil.success(resultMap);

    }

    /**
     * @description: 获取效果评估中 地市下拉选项
     * @author Mgpu
     * @return 地市下拉列表
     * @throws Exception
     */
    @GetMapping("/getCityListInit")
    public Result getCityListInit()throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<Map> cityMap = baseService.getEvaCityList();
        resultMap.put("cityOption", cityMap);
        return ResultUtil.success(resultMap);
    }

    /**
     * @description: 获取效果评估中 渠道下拉列表
     * @author Mgpu
     * @return 渠道下拉列表
     * @throws Exception
     */
    @RequestMapping("/getChanelListInit")
    @ResponseBody
    public Result getEvaCityListInit()throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<Map> channelMap = baseService.getChannelList();
        resultMap.put("channelOption", channelMap);
        return ResultUtil.success(resultMap);
    }

    /**
     * @description: 获取效果评估中 地市 渠道 展示指标 下拉选项
     * @author Mgpu
     * @return 地市渠道 展示指标 下拉列表
     * @throws Exception
     */
    @GetMapping("/getCityChanelIndexInit")
    public Result getCityChanelIndexInit()throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<Map> cityMap = baseService.getEvaCityList();
        List<Map> channelMap = baseService.getChannelList();
        List<Map> indexMap = baseService.getIndexList();

        resultMap.put("cityOption", cityMap);
        resultMap.put("channelOption", channelMap);
        resultMap.put("indexOption", indexMap);
        return ResultUtil.success(resultMap);
    }

    @GetMapping("/getSceneSmsTemplate")
    public Result getSceneSmsTemplate(@RequestParam String sceneId)throws Exception{
        String smsTemplate = baseService.getSceneSmsTemplate(sceneId);
        return ResultUtil.success(smsTemplate);
    }

    /**
     * 获取业务订购top15
     */
    @GetMapping("/getTop15Product")
    public Result getTop15Product(HttpSession session)throws Exception{
        Map resultMap = new HashMap<>();

        User user = UserUtil.getCurrentUser(session);
        String cityId = user.getCityId();

        List<Map> mapList = baseService.getTop15Product(cityId);
        resultMap.put("productList",mapList);
        return ResultUtil.success(resultMap);
    }


    /**
     * 判断是否是从经分跳转到首页
     * 获取PN值
     */
    @GetMapping("/isJumpFromJF")
    public Result getSessionKeyFirstJump(HttpSession session) throws Exception{
        Map res = new HashMap();
        User user = UserUtil.getCurrentUser(session);
        int isFirstJump = 0;
        Object firstJump = user.getIsFirstJump();
        if ( firstJump == null ){
            isFirstJump = 1;
            user.setIsFirstJump("yes");
        }
        res.put("isFirstJump",isFirstJump);
        return ResultUtil.success(res);
    }

    /**
     * 更换浏览器获取网址
     */
    @GetMapping("/getOtherBrowserURL")
    public Result getOtherBrowserURL(HttpSession session,@RequestParam("uuId") String curUUID) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String newUUID = UUID.randomUUID().toString().replaceAll("-","");

        stringRedisTemplate.opsForValue().set(newUUID,user.getPN(),60*3, TimeUnit.SECONDS);

        String url = otherBrowserUrl + newUUID;

        return ResultUtil.success(url);
    }

    /**
     * 获取实时位置类型选项
     * @return List<Map> 类型列表
     */
    @GetMapping("/getRealTimePositionType")
    public Result getRealTimePositionType(){
        List<Map> typeMap = baseService.getRealTimePositionType();
        return ResultUtil.success(typeMap);
    }

    /**
     * 获取实时位置详细信息
     * @param session
     * @param tagCode 类型编码
     * @param searchVal  搜索框输入值
     * @return
     */
    @GetMapping("/getRealTimePositionInfos")
    public Result getRealTimePositionInfos(HttpSession session,@RequestParam("tagCode") String tagCode,@RequestParam("searchVal") String searchVal){
        String cityId = UserUtil.getCurrentUser(session).getCityId();
        Map map = new HashMap<>();
        map.put("cityId",cityId);
        map.put("tagCode",tagCode);
        map.put("searchVal",searchVal);
        List<Map> infosMap = baseService.getRealTimePositionInfos(map);
        return ResultUtil.success(infosMap);
    }

}
