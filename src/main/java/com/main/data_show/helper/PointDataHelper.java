package com.main.data_show.helper;

import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import com.main.data_show.service.TaPointDataService;
import com.main.data_show.service.TaPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class PointDataHelper {

    @Autowired
    public TaPointDataService taPointDataService;

    @Autowired
    public ToolHelper toolHelper;

    //pointName 重复不存在的时候新增
    public int insertPoint(int pointId,String dateStr,String hourStr,String pointData) throws ParseException {
            //测试插入taPoint
            TaPointData pointDataVo = new TaPointData();
            pointDataVo.setCreateTime(toolHelper.makeDateByDateAndHour(dateStr,hourStr));
            pointDataVo.setPointId(pointId);
            pointDataVo.setDateShow(dateStr);
            pointDataVo.setHourShow(hourStr);
            pointDataVo.setPointData(pointData);
            return taPointDataService.insert(pointDataVo);
    }
}
