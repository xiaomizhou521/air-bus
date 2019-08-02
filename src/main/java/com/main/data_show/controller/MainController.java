package com.main.data_show.controller;

import com.main.data_show.consts.JspPageConst;
import com.main.data_show.helper.LoginHelper;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.helper.UserHelper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import com.main.data_show.service.TaPointService;
import com.main.data_show.service.TaUserService;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private TaUserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private ToolHelper toolHelper;

    @Autowired
    private TaPointService taPointService;

    @RequestMapping(value = "/")
    public String toLogin(HttpServletRequest request) throws Exception {
        TaUser curUser = loginHelper.getCurUser(request);
        if(curUser == null){
            //去登陆页面
            return JspPageConst.LOGIN_JSP_REDIRECT;
        }else{
            //去主页面
            return JspPageConst.REDIRECT_MAIN_JSP_REDIRECT;
        }
    }

    @RequestMapping(value = "/toLogOut")
    public String toLogOut(HttpServletRequest request) throws Exception {
       loginHelper.logOut(request);
       return JspPageConst.LOGIN_JSP_REDIRECT;
    }

    @RequestMapping(value = "/login/loginDo")
    public String loginDo(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        if(toolHelper.isEmpty(userName)||toolHelper.isEmpty(passWord)){
            request.setAttribute("message", "用户名或密码不能为空！");
            throw new Exception("用户名或密码不能为空！");
        }
        TaUser userByUserName = userService.findUserByUserName(userName);
        if(userByUserName == null){
            //去登陆页面
            request.setAttribute("message", "用户名或密码错误！");
            logger.info("userName:"+userName+",登陆失败，用户名或密码错误!");
            return JspPageConst.LOGIN_JSP_REDIRECT;
        }
        String realPw = userByUserName.getPassWord();
        String salt = userByUserName.getSalt();
        String loginMd5Pw = userHelper.getPassWorldMd5(passWord, salt);
        //密码不同回登陆页面
        if(!realPw.equals(loginMd5Pw)){
             //去登陆页面
            request.setAttribute("message", "用户名或密码错误！");
            logger.info("userName:"+userName+",登陆失败，用户名或密码错误!");
            return JspPageConst.LOGIN_JSP_REDIRECT;
        }
        //保存登陆信息到session中
        loginHelper.saveLoginInfoToSession(request,userByUserName);
        //去主页面
        return JspPageConst.REDIRECT_MAIN_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/redirect")
    public String redirectMain(HttpServletRequest request) throws Exception {
        TaUser curUser = loginHelper.getCurUser(request);
        request.setAttribute("nickname",curUser.getNickName());
        return JspPageConst.MAIN_JSP_REDIRECT;
    }

    @RequestMapping(value = "work/exportExcel")
    public String exportExcel(HttpServletRequest request) {
        return JspPageConst.EXPORT_EXCEL_JSP_REDIRECT;
    }

    @RequestMapping(value = "/sesion_error")
    public String showSesionError(HttpServletRequest request) {
        return JspPageConst.SESSION_ERR_JSP_REDIRECT;
    }



}
