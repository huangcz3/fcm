package com.asiainfo.fcm.controller;

import com.asiainfo.fcm.entity.Result;
import com.asiainfo.fcm.entity.User;
import com.asiainfo.fcm.enums.ResultEnum;
import com.asiainfo.fcm.util.ResultUtil;
import com.asiainfo.fcm.util.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/20.
 */

@RestController
@RequestMapping("/hgj")
public class HgjController {

    @GetMapping("/jumpHGJ")
    public Result jump(HttpSession session){

        User user = UserUtil.getCurrentUser(session);
        if(user != null){
            String userName = user.getUserName();
            return ResultUtil.success(userName);
        }else {
            return ResultUtil.error(ResultEnum.NOT_LOGIN);
        }

    }

    @RequestMapping("/MTool")
    public Result getMToolsMark(HttpSession session,HttpServletRequest request,HttpServletResponse resp,
                             @RequestParam("param") String param) throws IOException {
        User user = UserUtil.getCurrentUser(session);
        if(user != null) {
            session.setAttribute("MTools", param);
            resp.sendRedirect("fcm/vicActManage.html");
            return ResultUtil.success(param);
        }else {
            return ResultUtil.error(ResultEnum.NOT_LOGIN);
        }
    }


}
