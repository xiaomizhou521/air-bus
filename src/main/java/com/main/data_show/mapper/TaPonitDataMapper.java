package com.main.data_show.mapper;

import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import org.apache.ibatis.annotations.Param;

public interface TaPonitDataMapper {

    public int insertTaPointData(TaPointData taPointData);

}
