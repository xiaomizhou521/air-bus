package com.main.data_show.mapper;

import com.main.data_show.pojo.TaUsagePointDataDate;
import org.apache.ibatis.annotations.Param;

public interface TaUsagePonitDataDateMapper {

    public int insertTaInstantPointDataDate(TaUsagePointDataDate taUsagePointDataDate);

    public TaUsagePointDataDate getUsagePointDataDateByPointIdAndTime(@Param("point_id")int point_id,@Param("date_show")long date_show);

    public void updateTaPointDataDateByPointIdAndTime(TaUsagePointDataDate taUsagePointDataDate);

}
