package com.main.data_show.helper;

import com.main.data_show.mapper.TaAllPonitDataMapper;
import com.main.data_show.mapper.TaInstantPonitDataMapper;
import com.main.data_show.pojo.TaAllPointData;
import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.service.TaPointDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class InstantPointDataHelper {

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaInstantPonitDataMapper taInstantPonitDataMapper;

    //pointName 重复不存在的时候新增
    public int insertAllPoint(int pointId,String dateStr,String hourStr,String pointData,double pointUsage) throws ParseException {
            //测试插入taPoint
            TaInstantPointData allPointDataVo = new TaInstantPointData();
            allPointDataVo.setPointId(pointId);
            allPointDataVo.setCreateTime(toolHelper.makeDateByDateAndHour(dateStr,hourStr));
            allPointDataVo.setCreateTimeInt(toolHelper.dateToNumDate(allPointDataVo.getCreateTime()));
            allPointDataVo.setDateShow(dateStr);
            allPointDataVo.setHourShow(hourStr);
            allPointDataVo.setPointData(pointData);
            allPointDataVo.setPointUsage(pointUsage);
            return taInstantPonitDataMapper.insertTaInstantPointData(allPointDataVo);
    }
}
