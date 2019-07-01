package com.main.data_show;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("testBean")
public class TestBean {

    @RequestMapping("testDemo")
    public String testDemo(){
System.out.println("hello.jsp");
        return "hello";
    }
}
