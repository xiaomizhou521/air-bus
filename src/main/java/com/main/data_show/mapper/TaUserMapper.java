package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface  TaUserMapper {

    @Select("select * from user")
    List<TaUser> findAll();

    @Insert("insert into user(userId,userName,nickName)values(#{userId},#{userName},#{nickName}")
    public int add(TaUser user);
}
