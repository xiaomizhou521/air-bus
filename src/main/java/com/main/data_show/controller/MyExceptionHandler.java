package com.main.data_show.controller;

import com.main.data_show.exception.SessionLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {
    //将自定义的异常引入进来
    @ResponseBody
    @ExceptionHandler(value = SessionLoginException.class)
    public ModelAndView handleUserNotExistsException(SessionLoginException e) {
        ModelAndView modelAndView=new ModelAndView();
        Map<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        modelAndView.addObject("map",map);
        modelAndView.setViewName("sesion_error");
        return modelAndView;
    }
}
