package com.main.data_show.controller;

import com.main.data_show.helper.LoginHelper;
import com.main.data_show.mapper.TaUserMapper;
import com.main.data_show.pojo.TaUser;
import com.main.data_show.service.TaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TaUserController {
    @Autowired
    private TaUserService userService;

    @Autowired
    private LoginHelper loginHelper;

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
        TaUser userByUserName = userService.findUserByUserName(userName);
        String userPw = userByUserName.getPass_word();
        System.out.println("userPw:"+userPw);
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
