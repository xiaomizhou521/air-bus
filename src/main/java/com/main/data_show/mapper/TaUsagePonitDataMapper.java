package com.main.data_show.mapper;

import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.pojo.TaUsagePointData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaUsagePonitDataMapper {

    public int insertTaInstantPointData(TaUsagePointData taUsagePointData);

    public TaUsagePointData getUsagePointDataByPointIdAndTime(@Param("point_id")int point_id,@Param("create_time_int")long create_time_int);

    public List<TaUsagePointData> findUsagePointByPointIdAndTime(long startExpDate, long endExpDate, @Param("point_id") int pointId);

}
