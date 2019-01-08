package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.*;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.entity.IopSendOrder;
import com.asiainfo.fcm.service.IIOPActivityService;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 *全网活动
 * Created by Shaojinyu on 2017/8/29.
 */
@RestController
@RequestMapping("/iop")
public class IOPActivityController {

    @Autowired
    private IIOPActivityService iopActivityService;

    /**
     * 查询主活动
     */
    @GetMapping("/getActivityList")
    public Result getActivityList(HttpServletRequest request) throws Exception{
        Map resultMap = new HashMap();
        String searchVal = request.getParameter("searchVal");
        int currentPage = Integer.parseInt(request.getParameter("page"));
        int lengthRow = Integer.parseInt(request.getParameter("lengthRow"));
        int startRow = (currentPage -1)*10;

        User user = UserUtil.getCurrentUser(request.getSession());
        String userPhoneNo = user.getPhoneNo();

        Map parameterMap = new HashMap();
        parameterMap.put("searchVal",searchVal);
        parameterMap.put("currentPage",currentPage);
        parameterMap.put("lengthRow",lengthRow);
        parameterMap.put("startRow",startRow);
        parameterMap.put("userPhoneNo",userPhoneNo);

        List<IOPActivityInfo> activityList = iopActivityService.getActivityList(parameterMap);
        int totals = iopActivityService.getActivityListTotals(parameterMap);
        String authority = iopActivityService.getUserAuthority(userPhoneNo);

        resultMap.put("activityList",activityList);
        resultMap.put("totals",totals);
        resultMap.put("authority",authority);
        return ResultUtil.success(resultMap);
    }

    /**
     * 查询主活动下所有子活动的共有属性 -- 效果评估 和 PCC策略
     */
    @GetMapping("/getCampaginPublicAttrInfo")
    public Result getPublicAttrInfo(String activityId) throws Exception{
        Map resultMap = new HashMap();

        IOPActivityInfo publicAttrList = iopActivityService.getPublicAttrInfo(activityId);
        resultMap.put("publicAttrList",publicAttrList);
        return ResultUtil.success(resultMap);
    }

    /**
     * 查询主活动下子活动
     */
    @GetMapping("/getCampaginInfoById")
    public Result getCampaginInfoById(String activityId) throws Exception{
        Map resutMap = new HashMap();
        List<IOPCampaginInfo> campaginInfoList = iopActivityService.getCampaginInfo(activityId);

        resutMap.put("campaginInfoList",campaginInfoList);

        return ResultUtil.success(resutMap);
    }

    /**
     * 查询活动所属人
     */
    @GetMapping("/getCampaignBelonger")
    public Result getCampaignBelonger(@RequestParam String campaignId,@RequestParam String activityId ) throws Exception{
        Map parameterMap = new HashMap();
        parameterMap.put("activityId",activityId);
        parameterMap.put("campaignId",campaignId);

        Map belongerInfoMap = iopActivityService.getBelongerInfo(parameterMap);
        return ResultUtil.success(belongerInfoMap);
    }

    /**
     * 返回对应派单用户列表
     */
    @GetMapping("/getAllUserList")
    public Result getAllUserList() throws Exception{
        List<IOPUserInfo> userList = iopActivityService.getAllUserList();

        return ResultUtil.success(userList);
    }

    /**
     * 派单管理，将某活动派单下发某用户
     * @param activityId     下发主活动id
     * @param campaignId     下发子活动id
     * @param executorPhone  被派人手机号
     *  commanderPhone 指派人手机号
     * @return
     */
    @RequestMapping(value = "/setActivityInfoToUser",method = RequestMethod.POST)
    public Result setActivityInfoToUser(@RequestParam String activityId,@RequestParam String campaignId,@RequestParam String executorPhone,HttpSession session) throws Exception{

        IopSendOrder parameterMap = new IopSendOrder();

        User user = UserUtil.getCurrentUser(session);
        String userPhone = user.getPhoneNo();

        parameterMap.setActivityId(activityId);
        parameterMap.setCampaignId(campaignId);
        parameterMap.setExecutorPhone(executorPhone);
        parameterMap.setCommanderPhone(userPhone);
        parameterMap.setCampId("");

        IopSendOrder iopSendOrderInfo = iopActivityService.checkSendOrderInfo(parameterMap);
        if( iopSendOrderInfo != null ){
            return ResultUtil.error(ResultEnum.HAS_SENDER_ORDER_ERROR);
        }else{
            iopActivityService.insertIntoG2PActDownRel(parameterMap);

            //指派成功发送短信 = =。。
        }

        return ResultUtil.success();
    }


//    /**
//     *
//     * @param iopRelateCampagin
//     * @param campaginId    下发子活动id
//     * @param campId    本地创建活动id
//     * @param campName   本地创建活动名称
//     * @return
//     * @throws Exception
//     */
    @PostMapping("/saveRelateCampaginAndCamp")
    public Result saveRelateCampaginAndCamp(@RequestBody List<IOPRelateCampagin> iopRelateCampaginList) throws Exception{
        iopActivityService.insertIntoCampaginAndCampRel(iopRelateCampaginList);
        return ResultUtil.success();
    }

    /**
     * 查询子活动关联本地创建活动
     */
    @GetMapping("/getRelateCampaginAndCamp")
    public Result getRelateCampaginAndCamp(@RequestParam String campaignId) throws Exception{
        List<IOPRelateCampagin> getRelateCampaginAndCamp = iopActivityService.getRelateCampaginAndCamp(campaignId);
        return ResultUtil.success(getRelateCampaginAndCamp);
    }
}
