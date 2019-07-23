package com.main.data_show.mapper;

import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface  TaUserMapper {

    List<TaUser> findAll();

    TaUser findUserByUserName(@Param("user_name")String user_name);

    TaUser findUserByUserId(@Param("user_id")int user_id);

    public int add(TaUser user);
}
