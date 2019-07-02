package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface  TaUserMapper {

    List<TaUser> findAll();

    public int add(TaUser user);
}
