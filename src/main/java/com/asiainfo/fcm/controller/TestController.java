package com.asiainfo.fcm.controller;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;


//import com.asiainfo.fcm.entity.Result;
//import com.asiainfo.fcm.entity.User;
//import com.asiainfo.fcm.service.ITestService;
//import com.asiainfo.fcm.service.IUserService;
//import com.asiainfo.fcm.util.IDUtil;
//import com.asiainfo.fcm.util.ResultUtil;
//import com.asiainfo.fcm.util.UserUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.io.File;
//import java.util.*;
//
//@RestController
//@RequestMapping("/test")
public class TestController {
//
//    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
//
//    @Autowired
//    private ITestService testService;
//
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @GetMapping("/")
//    public Result test(HttpSession session) throws Exception {
////        Map<String, String> map = new HashMap<>();
////        map.put("id", "ai_duhongzhi");
////        map.put("name", "杜鸿志");
////        User user = (User) session.getAttribute("user");
////        System.out.print(session.getAttribute("user"));
////        System.out.print(stringRedisTemplate.opsForHash().entries("aaa"));
////        testService.addValue(map);
//
//        return ResultUtil.success(IDUtil.generateActivityId());
//    }
//
//    @PostMapping("/insert")
//    public void insertTestData(){
//        for (int i=0;i<206;i++){
//            Map map = new HashMap<>();
//            Random random = new Random();
//            map.put("CITY_ID",random.nextInt(22));
//            map.put("PRC_TYPE",2);
//            map.put("PRC_ID","ACAF"+random.nextInt(1000));
//            map.put("PRC_NAME","测活动试"+i+1);
//            map.put("OP_CODE",random.nextInt(10000));
//            map.put("OP_NAME","营销活动");
//            map.put("BUSI_TYPE","cs");
//            map.put("PARENT_BUSI_TYPE","测试附加资费");
//            testService.insertTestData(map);
//        }
//    }
//
//    @PostMapping("/updateSession")
//    public Result updateSession(HttpSession session, HttpServletRequest request){
//        User user = UserUtil.getCurrentUser(session);
//        String userId = request.getParameter("userId");
//        user.setUserId(userId);
//       return ResultUtil.success(user);
//    }
//
//
//    @GetMapping("/updateActivityState")
//    public Result updateActivityState(@RequestParam String activityId){
//        testService.udateState(activityId);
//        return ResultUtil.success();
//    }
//
//    @PostMapping("insertTestDatas")
//    public Result  insertTestDatas(HttpServletRequest request)throws Exception{
//        String data = request.getParameter("data");
//        data.replaceAll(File.separator,"");
//        JSONArray arry = JSONArray.fromObject(data);
//
//        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
//        for (int i = 0; i < arry.size(); i++)
//        {
//            JSONObject jsonObject = arry.getJSONObject(i);
//            Map<String, String> map = new HashMap<String, String>();
//            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
//            {
//
//                String key = (String) iter.next();
//                String value = jsonObject.get(key).toString();
//                map.put(key, value);
//            }
//            rsList.add(map);
//        }
//
//
//
//        return ResultUtil.success(rsList);
//    }
//
}
