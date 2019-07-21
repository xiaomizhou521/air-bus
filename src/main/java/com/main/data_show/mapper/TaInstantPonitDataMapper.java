package com.main.data_show.mapper;

import com.main.data_show.pojo.TaAllPointData;
import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.pojo.TaPointData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaInstantPonitDataMapper {

    public int insertTaInstantPointData(TaInstantPointData taInstantPointData);

    public List<TaInstantPointData> findInstantPointByPointIdAndTime(long startExpDate, long endExpDate, @Param("point_id") int pointId);


}
