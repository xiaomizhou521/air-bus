package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.pojo.TaUsagePointDataMon;
import org.apache.ibatis.annotations.Param;

public interface TaUsagePonitDataMonMapper {

    public int insertTaInstantPointDataMon(TaUsagePointDataMon taUsagePointDatamon);

    public TaUsagePointDataMon getUsagePointDataMonByPointIdAndTime(@Param("point_id")int point_id, @Param("date_show")long date_show);

    public void updateTaPointDataMonByPointIdAndTime(TaUsagePointDataMon taUsagePointDataDate);



}
