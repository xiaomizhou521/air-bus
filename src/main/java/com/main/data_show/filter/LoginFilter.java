package com.main.data_show.filter;


import com.main.data_show.exception.SessionLoginException;
import com.main.data_show.helper.LoginHelper;
import com.main.data_show.pojo.TaUser;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName="firstFilter", urlPatterns="/login/*")
public class LoginFilter implements Filter {

    @Autowired
    private LoginHelper loginHelper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
  /*      HttpServletRequest checkRequest = (HttpServletRequest) request;
        HttpServletResponse checkResponse = (HttpServletResponse) response;
        try {
            TaUser curUser = loginHelper.getCurUser(checkRequest);
            if(curUser == null){
                checkResponse.sendRedirect("/sesion_error");
            }else{
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        chain.doFilter(request, response);
    }
}
