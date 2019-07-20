package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.mapper.TaAllPonitDataMapper;
import com.main.data_show.mapper.TaUsagePonitDataMapper;
import com.main.data_show.pojo.TaAllPointData;
import com.main.data_show.pojo.TaUsagePointData;
import com.main.data_show.service.TaPointDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class UsagePointDataHelper {

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataMapper taUsagePonitDataMapper;

    //pointName 重复不存在的时候新增
    public int insertAllPoint(int pointId, String dateStr, String hourStr, String pointData, double pointUsage, Date dateTime, long dateTimeInt) throws ParseException {
            //测试插入taPoint
            TaUsagePointData allPointDataVo = new TaUsagePointData();
            allPointDataVo.setPointId(pointId);
            allPointDataVo.setCreateTime(dateTime);
            allPointDataVo.setCreateTimeInt(dateTimeInt);
            allPointDataVo.setDateShow(dateStr);
            allPointDataVo.setHourShow(hourStr);
            allPointDataVo.setPointData(pointData);
            allPointDataVo.setPointUsage(pointUsage);
            return taUsagePonitDataMapper.insertTaInstantPointData(allPointDataVo);
    }

    public TaUsagePointData getUsagePointDataByPointIdAndTime(int pointId,Date curDate){
        Date date = toolHelper.addSubHour(curDate, -1);
        long dateNum = toolHelper.dateToNumDate(date, SysConsts.DATE_FORMAT_3);
        return taUsagePonitDataMapper.getUsagePointDataByPointIdAndTime(pointId,dateNum);
    }
}
