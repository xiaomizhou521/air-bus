package com.main.data_show.service;

import com.main.data_show.mapper.TaUserMapper;
import com.main.data_show.pojo.TaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaUserService {

    @Resource
    private TaUserMapper userMapper;

    public List<TaUser> findAll(){
        return userMapper.findAll();
    }

    public TaUser findUserByUserName(String userName){
        return userMapper.findUserByUserName(userName);
    }

    public TaUser findUserByUserId(int userId){
        return userMapper.findUserByUserId(userId);
    }

    public int add(TaUser user){
        return userMapper.add(user);

    }
}
