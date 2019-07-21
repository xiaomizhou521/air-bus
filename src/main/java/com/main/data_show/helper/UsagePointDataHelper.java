package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.mapper.TaUsagePonitDataMapper;
import com.main.data_show.pojo.TaInstantPointData;
import com.main.data_show.pojo.TaUsagePointData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

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

    public List<TaUsagePointData> queryUsagePointDataSum(String startExpDate, String endExpDate, String takeTime, int pointId) throws ParseException {
        Date startExp = toolHelper.makeStrToDate(startExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);
        //开始时间应该 时选择的时间前一天开始统计  选择日期的用量
        Date subStartExp = toolHelper.addSubDate(startExp, -1);
        Date endExp = toolHelper.makeStrToDate(endExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);
        //单个取每个点的数据
        long startExportTimeNum = toolHelper.dateToNumDate(subStartExp, SysConsts.DATE_FORMAT_3);
        long endExportTimeNum = toolHelper.dateToNumDate(endExp, SysConsts.DATE_FORMAT_3);
        return taUsagePonitDataMapper.findUsagePointByPointIdAndTime(startExportTimeNum,endExportTimeNum,pointId);
    }
}
