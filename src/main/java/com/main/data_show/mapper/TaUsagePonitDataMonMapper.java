package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.pojo.TaUsagePointDataMon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaUsagePonitDataMonMapper {

    public int insertTaInstantPointDataMon(TaUsagePointDataMon taUsagePointDatamon);

    public TaUsagePointDataMon getUsagePointDataMonByPointIdAndTime(@Param("point_id")int point_id, @Param("date_show")long date_show);

    public void updateTaPointDataMonByPointIdAndTime(TaUsagePointDataMon taUsagePointDataDate);

    public List<TaUsagePointDataMon> findUsagePointDataMonByPointIdAndTime(long startExpDate, long endExpDate, @Param("pointIds")String pointIds);



}
