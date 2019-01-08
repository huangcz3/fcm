package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ChanEnum;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.enums.SmsOpEnum;
import com.asiainfo.fcm.service.ISmsManageService;
import com.asiainfo.fcm.util.DateUtil;
import com.asiainfo.fcm.util.ExcelUtil;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import com.asiainfo.sms.api.bean.QuerySmsParm;
import com.asiainfo.sms.api.bean.QuerySmsResult;
import com.asiainfo.sms.client.Client;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chengzhongtao on 2017/12/13.
 */
@RestController
@RequestMapping("/smsManage")
public class SmsManageController {

    private final static Logger logger = LoggerFactory.getLogger(SmsManageController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ISmsManageService smsManageService;

    @Autowired
    @Qualifier("smsClient")
    private Client smsClient;

    @Autowired
    @Qualifier("yhtxClient")
    private Client yhtxClient;

    /**
     *  操作列表信息展示
     * @param searchVal 查询字段
     * @param channelId 执行渠道 0：全部渠道
     *                           1：10086群发渠道
     *                           2：优惠渠道
     * @param status 活动状态   0:全部状态
                                1）发送中
                                2）待发送
                                3）发送完成
                                4）活动结束

     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param currentPage 当前页
     * @param lengthRow 每页条数
     */
    @RequestMapping("/getActivityList")
    @ResponseBody
    public Result getActivityList(String searchVal, String channelId, String status, String startTime,
                                  String endTime, int currentPage, int lengthRow, HttpServletRequest request) throws Exception{
        try {
            User user = UserUtil.getCurrentUser(request.getSession());
            String userRole = user.getUserRole();

            boolean power = getUserPower(request.getSession());
            QuerySmsParm param = new QuerySmsParm();
            //封装参数
            param.setBusinessIds(ChanEnum.valueOf(channelId).getMsg());
            if(!"".equals(searchVal)){
                param.setContent(searchVal);
                param.setIdNo(searchVal);
            }
            param.setState(Integer.valueOf(status)); //活动状态
            param.setCurPage(currentPage);//当前页
            param.setPageNum(lengthRow);//每页条数
            if(startTime != ""){
                param.setStartTime(startTime.replace("-",""));//开始时间
            }
            if(endTime != ""){
                param.setEndTime(endTime.replace("-",""));//结束时间
            }

            List<QuerySmsResult> result = smsClient.querySmsSendInfo(param);
            try {
                JSONArray json = JSONArray.fromObject(result);
                logger.info(json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            for( QuerySmsResult row : result ){
                String id = row.getIdNo();
                //保存活动名
                String name = (String)stringRedisTemplate.opsForHash().entries("ACTIVE:" + id).get("ACTIVENAME");
                row.setIdNoName(name);
                Map<Object, Object> num = stringRedisTemplate.opsForHash().entries("HASH_SMS_PLATFORM_SEND_COUNT:" + id);
                int all = 0;
                if(num.get("all") != null && !"".equals(num.get("all"))){
                    all = Integer.valueOf(num.get("all").toString());
                }
                int successNum = 0;
                if(num.get("success") != null && !"".equals(num.get("success"))){
                    successNum = Integer.valueOf(num.get("success").toString());
                }
                int failNum = 0;
                if(num.get("fail") != null && !"".equals(num.get("fail"))){
                    failNum = Integer.valueOf(num.get("fail").toString());
                }
                int sendedNum = successNum + failNum;
                int remainNum = all - sendedNum;
                row.setSuccessNum(successNum);
                row.setFailNum(failNum);
                row.setSendedNum(sendedNum);
                row.setRemainNum(remainNum);
                logger.info(",all:" + all + ",successNum:" + successNum + ",failNum" + failNum);
                //获取成功发送量
                //时间戳转YYYY-MM-DD HH:MM:SS
                if(row.getOpTime() != null && !"".equals(row.getOpTime())){
                    row.setOpTime(DateUtil.dateToStamp(row.getOpTime()));
                }
                //YYYYMMDD 转 YYYY-MM-DD
                row.setStartTime(DateUtil.strToDateFormat(row.getStartTime()));
                row.setEndTime(DateUtil.strToDateFormat(row.getEndTime()));
            }
            //数据总条数
            int totalNum = smsClient.querySmsSendAllNum(param);
            Map<String,Object> r = new HashMap<>();
            r.put("total",totalNum);
            r.put("result", result);
            r.put("sendOperator", power);
            r.put("userRole",userRole);
            return ResultUtil.success(r);
        } catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *  操作列表信息展示
     * @param activityId 活動ID
     */
    @RequestMapping("/showOperateList")
    @ResponseBody
    public Result showOperateList(String activityId) throws Exception{
        try {
            return ResultUtil.success(smsManageService.getOperateList(activityId));
        } catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *  改变发送速率
     * @param activityId 活動ID
     * @param speed 修改速率
     * @param reason 修改原因
     */
    @RequestMapping("/changeActivitySpeed")
    @ResponseBody
    public Result changeActivitySpeed(HttpServletRequest request, String activityId, String speed, String reason, String businessIds) throws Exception{
        try {
            if(!getUserPower(request.getSession())){
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
            String rel  = "1";
            if("10086".equals(businessIds)){
                rel  = smsClient.setSendRateControl(activityId, Integer.valueOf(speed));
            }else {
                rel  = yhtxClient.setSendRateControl(activityId, Integer.valueOf(speed));
            }

            logger.info(rel);
            //保存操作原因
            User user = UserUtil.getCurrentUser(request.getSession());
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.CHANGE, reason, rel.equals("0") ? 1 : 0);
            return rel.equals("0") ? ResultUtil.success() : ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }  catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *  改变活动顺序
     * @param activityId 活動ID
     * @param reason 修改原因
     */
    @RequestMapping("/advanceActivity")
    @ResponseBody
    public Result advanceActivity(String activityId, String reason, String businessIds, HttpServletRequest request) throws Exception{
        try {
            if(!getUserPower(request.getSession())){
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
            String rel  = "1";
            if("10086".equals(businessIds)){
                rel  = smsClient.top(activityId);;
            }else {
                rel  = yhtxClient.top(activityId);;
            }
            logger.info(rel);
            //保存操作原因
            User user = UserUtil.getCurrentUser(request.getSession());
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.JUMP, reason, rel.equals("0") ? 1 : 0);
            return rel.equals("0") ? ResultUtil.success() : ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }  catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *  暂停活动
     * @param activityId 活動ID
     * @param reason 修改原因
     */
    @RequestMapping("/pausedActivity")
    @ResponseBody
    public Result pausedActivity(String activityId, String reason, String businessIds, HttpServletRequest request) throws Exception{
        try {
            if(!getUserPower(request.getSession())){
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
            String rel  = "1";
            if("10086".equals(businessIds)){
                rel  = smsClient.setSendPause(activityId,3);
            }else {
                rel  = yhtxClient.setSendPause(activityId,3);
            }
            logger.info(rel);
            //保存操作原因
            User user = UserUtil.getCurrentUser(request.getSession());
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.PARSE, reason, rel.equals("0") ? 1 : 0);
            return rel.equals("0") ? ResultUtil.success() : ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }  catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *  恢复活动
     * @param activityId 活動ID
     * @param reason 修改原因
     */
    @RequestMapping("/recoveryActivity")
    @ResponseBody
    public Result recoveryActivity(String activityId, String reason, String businessIds, HttpServletRequest request) throws Exception{
        try {
            if(!getUserPower(request.getSession())){
                return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
            }
            String rel  = "1";
            if("10086".equals(businessIds)){
                rel  = smsClient.setSendPause(activityId,0);
            }else {
                rel  = yhtxClient.setSendPause(activityId,0);
            }
            logger.info(rel);
            //保存操作原因
            User user = UserUtil.getCurrentUser(request.getSession());
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.RECOVERY, reason, rel.equals("0") ? 1 : 0);
            return rel.equals("0") ? ResultUtil.success() : ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }  catch (IllegalArgumentException e) {
            logger.error(e.toString());
            return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }
    }

    /**
     *
     * @param activityId 活動ID
     * @param businessIds 渠道标识
     * @param sendState 活动状态
     * @param isFilter  是否过滤策略
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/changeIsFilter")
    @ResponseBody
    public Result changeSendPolicy(String activityId, String businessIds, String sendState,String isFilter, HttpSession session) throws Exception{
        User user = UserUtil.getCurrentUser(session);
        String role = user.getUserRole();
        int isFilt = Integer.parseInt(isFilter);
        if( role != null && !role.contains("singleOperator") ){
            return ResultUtil.error(ResultEnum.PERMISSION_DENIED);
        }
        if( "2".equals(sendState) || "4".equals(sendState) ){  //待发送 和 发送暂停
            String changeRes = "1";
            changeRes  = smsClient.setFilter(activityId,isFilt);

            logger.info(changeRes);
            //保存修改操作
            String reason2 = "更改是否经过策略过滤";
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.CHANGE_FILTER, reason2, changeRes.equals("0") ? 1 : 0);
            return changeRes.equals("0") ? ResultUtil.success("1") : ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
        }else if ("1".equals(sendState)){      //发送中，先暂停活动
            String rel  = "1";
            if("10086".equals(businessIds)){
                rel  = smsClient.setSendPause(activityId,3);
            }else {
                rel  = yhtxClient.setSendPause(activityId,3);
            }
            logger.info(rel);
            if (!rel.equals("0")){  //暂停活动失败
                return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
            }

            //活动暂停成功，更改策略
            String changeRes = "1";
            changeRes  = smsClient.setFilter(activityId,isFilt);
            logger.info(changeRes);
            //保存修改操作
            String reason2 = "更改是否经过策略过滤";
            smsManageService.saveOperateInfo(activityId, user, 2, SmsOpEnum.CHANGE_FILTER, reason2, changeRes.equals("0") ? 1 : 0);

            if (!changeRes.equals("0")){  //修改策略失败,恢复活动
                String res  = "1";
                if("10086".equals(businessIds)){
                    res  = smsClient.setSendPause(activityId,0);
                }else {
                    res  = yhtxClient.setSendPause(activityId,0);
                }
                logger.info(res);
                return ResultUtil.error(ResultEnum.UNKNOWN_ERROR);
            }
            return ResultUtil.success("2");
        }else {
            return ResultUtil.error(ResultEnum.ACTIVITY_HAS_FINISHED);
        }
    }

    /**
     *  下载
     */
    @GetMapping("/getActivityListDownload")
    public Result getActivityListDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //获取参数
        String searchVal = request.getParameter("searchVal");
        String channelId = request.getParameter("channelId");
        String status = request.getParameter("status");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        QuerySmsParm param = new QuerySmsParm();
        //封装参数
        param.setBusinessIds(ChanEnum.valueOf(channelId).getMsg());
        if(!"".equals(searchVal)){
            param.setContent(searchVal);
            param.setIdNo(searchVal);
        }
        param.setState(Integer.valueOf(status)); //活动状态
        if(startTime != ""){
            param.setStartTime(startTime.replace("-",""));//开始时间
        }
        if(endTime != ""){
            param.setEndTime(endTime.replace("-",""));//结束时间
        }

        List<Map<String,String>> list = new ArrayList<>();
        Map<String, String> m;

        //获取列表数据
        List<QuerySmsResult> result = smsClient.export(param);
        try {
            JSONArray json = JSONArray.fromObject(result);
            logger.info(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for( QuerySmsResult row : result ){
            m = new HashMap<>();
            String id = row.getIdNo();
            //活动ID
            m.put("activityId",id);
            //活动名
            m.put("activityName",(String)stringRedisTemplate.opsForHash().entries("ACTIVE:" + row.getIdNo()).get("ACTIVENAME"));
            Map<Object, Object> num = stringRedisTemplate.opsForHash().entries("HASH_SMS_PLATFORM_SEND_COUNT:" + id);
            int all = 0;
            if(num.get("all") != null && !"".equals(num.get("all"))){
                all = Integer.valueOf(num.get("all").toString());
            }
            int successNum = 0;
            if(num.get("success") != null && !"".equals(num.get("success"))){
                successNum = Integer.valueOf(num.get("success").toString());
            }
            int failNum = 0;
            if(num.get("fail") != null && !"".equals(num.get("fail"))){
                failNum = Integer.valueOf(num.get("fail").toString());
            }
            int sendedNum = successNum + failNum;
            int remainNum = all - sendedNum;
            m.put("successNum",String.valueOf(successNum));
            m.put("failNum",String.valueOf(failNum));
            m.put("sendedNum",String.valueOf(sendedNum));
            m.put("remainNum",String.valueOf(remainNum));
            //活动状态
            switch (row.getSendState()){
                case 1 :
                    m.put("activityStatus","发送中");
                    break;
                case 2 :
                    m.put("activityStatus","待发送");
                    break;
                case 3:
                    m.put("activityStatus","发送完成");
                    break;
                case 4:
                    m.put("activityStatus","活动结束");
                    break;
                default:
                    m.put("activityStatus","状态异常");
            }
            m.put("startTime", DateUtil.strToDateFormat(row.getStartTime()));
            m.put("endTime",DateUtil.strToDateFormat(row.getEndTime()));
            //时间戳转YYYY-MM-DD HH:MM:SS
            if(row.getOpTime() != null && !"".equals(row.getOpTime())){
                m.put("opTime",DateUtil.dateToStamp(row.getOpTime()));
            }else{
                m.put("opTime","");
            }
            m.put("speed",String.valueOf(row.getSpeed()));
            m.put("statu",row.getStatus() == 0 ? "暂停" : "恢复");
            m.put("sms",row.getContent());

            list.add(m);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(new Date());
        String name = "发送列表_";
        String sheetName = name+nowTime;

        //导出excel
        String [][] header = {{"活动ID","活动名称","活动状态","开始时间","结束时间","开始发送时间","已发送量","成功量","失败量","剩余量","速率","发送状态","短信內容"}};
        String columns = "activityId,activityName,activityStatus,startTime,endTime,opTime,sendedNum,successNum,failNum,remainNum,speed,statu,sms";
        exportComplexExcel(response,sheetName,header,columns,list);

        return ResultUtil.success();

    }

    public static <T> void exportComplexExcel(HttpServletResponse response, String sheetName, String[][] header,String columns, List<T> data) throws Exception {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexecl");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(sheetName.getBytes("gb2312"), "ISO8859-1") + ".xls");
        OutputStream os = response.getOutputStream();
        ExcelUtil excelImport = new ExcelUtil();
        excelImport.createBook(os);
        if (data != null && data.size() > 0 && !(data.get(0) instanceof Map))
            excelImport.writerSheetWithObject(sheetName, data,
                    columns.split(","), header);
        else
            excelImport.writerSheet(sheetName,
                    (List<Map<String, String>>) data, columns
                            .split(","), header);
        excelImport.closeBook();
        os.close();
    }

    /**
     *  获取用户权限
     */
    private static boolean getUserPower(HttpSession session){
        String roleInfo = UserUtil.getCurrentUser(session).getUserRole();
        return (roleInfo != null && roleInfo.contains("sendOperator"));
    }

}
