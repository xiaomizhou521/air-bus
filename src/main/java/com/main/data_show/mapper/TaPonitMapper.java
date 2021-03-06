package com.main.data_show.mapper;

import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaPonitMapper {

    public int insertTaPoint(TaPoint taPoint);

    public void updateTaPointByPointID(TaPoint taPoint);

    public void deleteTaPointByPointId(TaPoint taPoint);

    public void updateTaPointByPointName(TaPoint taPoint);

    TaPoint findPointByPointName(@Param("point_name")String pointName);

    List<TaPoint> likePointByPointName(@Param("point_name")String pointName);

    TaPoint findPointByPointId(@Param("point_id")int pointId);

    List<TaPoint> getPointsByPage(@Param("point_name")String pointName,@Param("remarks_name")String remarksName,@Param("point_type")String pointType);

    List<TaPoint> getPointsByPageParam(@Param("point_name")String pointName,@Param("remarks_name")String remarksName,@Param("point_type")String pointType,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    List<TaPoint> getPointsByPointIds(@Param("pointIds") String pointIds);

    List<TaPoint> getAllPointRelativePath();

    long getAllPointCount();
}
