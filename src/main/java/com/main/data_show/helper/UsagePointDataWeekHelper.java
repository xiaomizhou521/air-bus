package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataMonMapper;
import com.main.data_show.mapper.TaUsagePonitDataWeekMapper;
import com.main.data_show.pojo.TaUsagePointDataMon;
import com.main.data_show.pojo.TaUsagePointDataWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class UsagePointDataWeekHelper {

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataWeekMapper taUsagePonitDataWeekMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointWeek(int pointId, double pointUsage, Date dateTime) throws Exception {
            //先计算要放入数据库时间数字
          long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.week.toString());
            //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
          TaUsagePointDataWeek usagPointDateDateVo = taUsagePonitDataWeekMapper.getUsagePointDataWeekByPointIdAndTime(pointId, usageDateWeekMonNum);
          if(usagPointDateDateVo == null){
              TaUsagePointDataWeek vo = new TaUsagePointDataWeek();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              vo.setPointData(pointUsage);
              taUsagePonitDataWeekMapper.insertTaInstantPointDataWeek(vo);
          }else{
              TaUsagePointDataWeek vo = new TaUsagePointDataWeek();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              double newResult = toolHelper.doubleSum(pointUsage, usagPointDateDateVo.getPointData());
              vo.setPointData(newResult);
              taUsagePonitDataWeekMapper.updateTaPointDataWeekByPointIdAndTime(vo);
          }
    }

    //用量 周 导出图
    public List<TaUsagePointDataWeek> queryUsagePointDataWeekSum(String startExpDate, String endExpDate, String pointIds) throws ParseException {
        int startExportTimeNum = Integer.parseInt(startExpDate);
        int endExportTimeNum = Integer.parseInt(endExpDate);
        return taUsagePonitDataWeekMapper.findUsagePointDataWeekByPointIdAndTime(startExportTimeNum,endExportTimeNum,pointIds);
    }


}
