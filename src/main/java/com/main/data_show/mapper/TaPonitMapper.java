package com.main.data_show.mapper;

import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaPonitMapper {

    public int insertTaPoint(TaPoint taPoint);

    TaPoint findPointByPointName(@Param("point_name")String pointName);

    List<TaPoint> getPointsByPage();
}