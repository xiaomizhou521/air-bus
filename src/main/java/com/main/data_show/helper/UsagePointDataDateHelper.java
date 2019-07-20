package com.main.data_show.helper;

import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataDateMapper;
import com.main.data_show.mapper.TaUsagePonitDataMapper;
import com.main.data_show.pojo.TaUsagePointData;
import com.main.data_show.pojo.TaUsagePointDataDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class UsagePointDataDateHelper {

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataDateMapper taUsagePonitDataDateMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointDate(int pointId, double pointUsage, Date dateTime) throws Exception {
            //先计算要放入数据库时间数字
          long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.date.toString());
            //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
          TaUsagePointDataDate usagPointDateDateVo = taUsagePonitDataDateMapper.getUsagePointDataDateByPointIdAndTime(pointId, usageDateWeekMonNum);
          if(usagPointDateDateVo == null){
              TaUsagePointDataDate vo = new TaUsagePointDataDate();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              vo.setPointData(pointUsage);
              taUsagePonitDataDateMapper.insertTaInstantPointDataDate(vo);
          }else{
              TaUsagePointDataDate vo = new TaUsagePointDataDate();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              double newResult = toolHelper.doubleSum(pointUsage, usagPointDateDateVo.getPointData());
              vo.setPointData(newResult);
              taUsagePonitDataDateMapper.updateTaPointDataDateByPointIdAndTime(vo);
          }
    }


}
