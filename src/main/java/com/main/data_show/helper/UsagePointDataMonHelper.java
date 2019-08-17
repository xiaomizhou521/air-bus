package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataDateMapper;
import com.main.data_show.mapper.TaUsagePonitDataMonMapper;
import com.main.data_show.pojo.TaUsagePointDataDate;
import com.main.data_show.pojo.TaUsagePointDataMon;
import com.main.data_show.pojo.TaUsagePointDataWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsagePointDataMonHelper {
    private static String INTERVAL_CUR_READ_CSV_PATH_MON = "";

    public static Map<String, TaUsagePointDataMon> INTERVAL_CUR_FILE_MON_DATE = new HashMap<String,TaUsagePointDataMon>();

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataMonMapper taUsagePonitDataMonMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointMon(int pointId, double pointUsage, Date dateTime,String filePath) throws Exception {
            //先计算要放入数据库时间数字
          long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.mon.toString());

        //为了优化读取速度  在缓存里面计算日周月的   每个文件每个点每个时间只保存一次
        //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath相同  说明是同一个文件  在内存中记录数据
        String key = pointId + "@"+ usageDateWeekMonNum;
        if(INTERVAL_CUR_READ_CSV_PATH_MON.equals(filePath)){
            if(INTERVAL_CUR_FILE_MON_DATE.containsKey(key)){
                TaUsagePointDataMon vo = INTERVAL_CUR_FILE_MON_DATE.get(key);
                double newResult = toolHelper.doubleSum(pointUsage, vo.getPointData());
                vo.setPointData(newResult);
                INTERVAL_CUR_FILE_MON_DATE.put(key,vo);
            }else{
                TaUsagePointDataMon vo = new TaUsagePointDataMon();
                vo.setPointId(pointId);
                vo.setDateShow(usageDateWeekMonNum);
                vo.setPointData(pointUsage);
                INTERVAL_CUR_FILE_MON_DATE.put(key,vo);
            }
        }else{
            //缓存中的数据 读入到数据库中
         //   doIntervalMonCsv();
            INTERVAL_CUR_READ_CSV_PATH_MON = filePath;
            //清掉缓存后 放入 此次新读入的数据
            TaUsagePointDataMon vo = new TaUsagePointDataMon();
            vo.setPointId(pointId);
            vo.setDateShow(usageDateWeekMonNum);
            vo.setPointData(pointUsage);
            INTERVAL_CUR_FILE_MON_DATE.put(key,vo);
        }

    }

    //缓存中的数据 读入到数据库中 然后清空缓存
    public void doIntervalMonCsv(){
        //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath不相同 说明是新读取的文件 要把之前内存中记录的数据插入数据库 然后清除缓存 重新计算
        for(Map.Entry<String,TaUsagePointDataMon> map : INTERVAL_CUR_FILE_MON_DATE.entrySet()){
            TaUsagePointDataMon value = map.getValue();
            //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
            TaUsagePointDataMon usagPointDateDateVo = taUsagePonitDataMonMapper.getUsagePointDataMonByPointIdAndTime(value.getPointId(), value.getDateShow());
            if(usagPointDateDateVo == null){
                taUsagePonitDataMonMapper.insertTaInstantPointDataMon(value);
            }else{
                double newResult = toolHelper.doubleSum(value.getPointData(), usagPointDateDateVo.getPointData());
                value.setPointData(newResult);
                taUsagePonitDataMonMapper.updateTaPointDataMonByPointIdAndTime(value);
            }
        }
        //循环完了 修改路径  要清掉缓存  很重要 很重要
        INTERVAL_CUR_FILE_MON_DATE.clear();
    }

    //用量 日 导出图
    public List<TaUsagePointDataMon> queryUsagePointDataMonSum(String startExpDate, String endExpDate, String pointIds) throws ParseException {
        Date startExp = toolHelper.makeStrToDate(startExpDate, SysConsts.DATE_FORMAT_8);
        Date endExp = toolHelper.makeStrToDate(endExpDate, SysConsts.DATE_FORMAT_8);
        //单个取每个点的数据
        long startExportTimeNum = toolHelper.dateToNumDate(startExp, SysConsts.DATE_FORMAT_5);
        long endExportTimeNum = toolHelper.dateToNumDate(endExp, SysConsts.DATE_FORMAT_5);
        return taUsagePonitDataMonMapper.findUsagePointDataMonByPointIdAndTime(startExportTimeNum,endExportTimeNum,pointIds);
    }


}
