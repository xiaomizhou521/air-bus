package com.main.data_show.controller;

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

    @RequestMapping(value = "findAll")
    public String findAll(HttpServletRequest request) {

        System.out.println("PageController:page()");

        List<TaUser> list = userService.findAll();


        request.setAttribute("userlist", list);

        return "user_list";


    }
}
