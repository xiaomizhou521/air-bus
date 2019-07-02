package com.main.data_show.controller;

import cn.com.enorth.utility.Beans;
import com.main.data_show.DataShowApplication;
import com.main.data_show.helper.LoginHelper;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.helper.UserHelper;
import com.main.data_show.mapper.TaUserMapper;
import com.main.data_show.pojo.TaUser;
import com.main.data_show.service.TaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TaUserController {
    private static Logger logger = LoggerFactory.getLogger(TaUserController.class);
    @Autowired
    private TaUserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private ToolHelper toolHelper;

    @RequestMapping(value = "toLogin")
    public String toLogin(HttpServletRequest request) throws Exception {
        TaUser curUser = loginHelper.getCurUser(request);
        if(curUser == null){
            //去登陆页面
            return "login";
        }else{
            //去主页面
            return "main";
        }
    }

    @RequestMapping(value = "loginDo")
    public String loginDo(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        if(toolHelper.isEmpty(userName)||toolHelper.isEmpty(passWord)){
            request.setAttribute("message", "用户名或密码不能为空！");
            throw new Exception("用户名或密码不能为空！");
        }
        TaUser userByUserName = userService.findUserByUserName(userName);
        String realPw = userByUserName.getPass_word();
        String salt = userByUserName.getSalt();
        String loginMd5Pw = userHelper.getPassWorldMd5(passWord, salt);
        //密码不同回登陆页面
        if(!realPw.equals(loginMd5Pw)){
             //去登陆页面
            request.setAttribute("message", "用户名或密码错误！");
            logger.info("userName:"+userName+",登陆失败，用户名或密码错误!");
            return "login";
        }
        //去主页面
        return "main";
    }

    @RequestMapping(value = "findAll")
    public String findAll(HttpServletRequest request) {
        List<TaUser> list = userService.findAll();
        request.setAttribute("userlist", list);
        return "user_list";
    }
}
