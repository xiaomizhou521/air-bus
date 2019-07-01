package com.main.data_show.service;

import com.main.data_show.mapper.TaUserMapper;
import com.main.data_show.pojo.TaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaUserService {

    @Autowired
    private TaUserMapper userMapper;

    public List<TaUser> findAll(){


        return userMapper.findAll();
    }

    public int add(TaUser user){

        return userMapper.add(user);

    }
}
