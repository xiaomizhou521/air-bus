package com.main.data_show.helper;

import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataDateMapper;
import com.main.data_show.mapper.TaUsagePonitDataMonMapper;
import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.pojo.TaUsagePointDataMon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UsagePointDataMonHelper {

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataMonMapper taUsagePonitDataMonMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointMon(int pointId, double pointUsage, Date dateTime) throws Exception {
            //先计算要放入数据库时间数字
          long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.mon.toString());
            //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
          TaUsagePointDataMon usagPointDateDateVo = taUsagePonitDataMonMapper.getUsagePointDataMonByPointIdAndTime(pointId, usageDateWeekMonNum);
          if(usagPointDateDateVo == null){
              TaUsagePointDataMon vo = new TaUsagePointDataMon();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              vo.setPointData(pointUsage);
              taUsagePonitDataMonMapper.insertTaInstantPointDataMon(vo);
          }else{
              TaUsagePointDataMon vo = new TaUsagePointDataMon();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              double newResult = toolHelper.doubleSum(pointUsage, usagPointDateDateVo.getPointData());
              vo.setPointData(newResult);
              taUsagePonitDataMonMapper.updateTaPointDataMonByPointIdAndTime(vo);
          }
    }


}
