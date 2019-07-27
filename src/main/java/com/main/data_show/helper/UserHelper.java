package com.main.data_show.helper;

import cn.com.enorth.utility.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserHelper {
    @Autowired
    private ToolHelper toolHelper;

    //计算密码
    public String getPassWorldMd5(String password,String salt){
        String temp = toolHelper.getMD5(password) + toolHelper.getMD5(salt);
        return toolHelper.getMD5(temp);
    }

}
