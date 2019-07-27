package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.pojo.TaUsagePointDataWeek;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaUsagePonitDataWeekMapper {

    public int insertTaInstantPointDataWeek(TaUsagePointDataWeek taUsagePointDataWeek);

    public TaUsagePointDataWeek getUsagePointDataWeekByPointIdAndTime(@Param("point_id")int point_id, @Param("date_show")long date_show);

    public void updateTaPointDataWeekByPointIdAndTime(TaUsagePointDataWeek taUsagePointDataDate);

    public List<TaUsagePointDataWeek> findUsagePointDataWeekByPointIdAndTime(@Param("startExpDate")long startExpDate, @Param("endExpDate")long endExpDate, @Param("pointIds")String pointIds);



}
