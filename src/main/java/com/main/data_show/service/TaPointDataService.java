package com.main.data_show.service;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaPonitDataMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import org.apache.catalina.startup.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaPointDataService {

    @Resource
    private TaPonitDataMapper taPonitDataMapper;
    @Autowired
    private ToolHelper toolHelper;

    public int insert(TaPointData taPointData){
        return taPonitDataMapper.insertTaPointData(taPointData);
    }

    public List<TaPointData> queryPointData(String startExpDate, String endExpDate, String pointIds){
        return taPonitDataMapper.queryPointData(startExpDate,endExpDate,pointIds);
    }

    public List<TaPointData> queryPointDataSum(String startExpDate, String endExpDate,String takeTime, String pointIds) throws ParseException {
        Date startExp = toolHelper.makeStrToDate(startExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);
        //开始时间应该 时选择的时间前一天开始统计  选择日期的用量
        Date subStartExp = toolHelper.addSubDate(startExp, -1, SysConsts.DATE_FORMAT_1);
        Date endExp = toolHelper.makeStrToDate(endExpDate+" "+takeTime, SysConsts.DATE_FORMAT_1);
        return taPonitDataMapper.queryPointDataSum(subStartExp,endExp,pointIds);
    }

    public List<TaPointData> queryPointDeviceChart(String startExpDate, String endExpDate,String pointIds) throws ParseException {
        Date startExp = toolHelper.makeStrToDate(startExpDate, SysConsts.DATE_FORMAT_1);
        Date endExp = toolHelper.makeStrToDate(endExpDate, SysConsts.DATE_FORMAT_1);
        return taPonitDataMapper.queryPointDeviceChart(startExp,endExp,pointIds);
    }

}
