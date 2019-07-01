package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface  TaUserMapper {

    @Select("select * from ta_user")
    List<TaUser> findAll();

    @Insert("insert into ta_user(user_id,user_name,nick_name)values(#{userId},#{userName},#{nickName}")
    public int add(TaUser user);
}
