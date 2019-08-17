package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataMonMapper;
import com.main.data_show.mapper.TaUsagePonitDataWeekMapper;
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
public class UsagePointDataWeekHelper {

    private static String INTERVAL_CUR_READ_CSV_PATH_WEEK = "";

    public static Map<String, TaUsagePointDataWeek> INTERVAL_CUR_FILE_WEEK_DATE = new HashMap<String,TaUsagePointDataWeek>();

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataWeekMapper taUsagePonitDataWeekMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointWeek(int pointId, double pointUsage, Date dateTime,String filePath) throws Exception {
        //先计算要放入数据库时间数字
        long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.week.toString());
        //为了优化读取速度  在缓存里面计算日周月的   每个文件每个点每个时间只保存一次
        //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath相同  说明是同一个文件  在内存中记录数据
        String key = pointId + "@" + usageDateWeekMonNum;
        if (INTERVAL_CUR_READ_CSV_PATH_WEEK.equals(filePath)) {
            if (INTERVAL_CUR_FILE_WEEK_DATE.containsKey(key)) {
                TaUsagePointDataWeek vo = INTERVAL_CUR_FILE_WEEK_DATE.get(key);
                double newResult = toolHelper.doubleSum(pointUsage, vo.getPointData());
                vo.setPointData(newResult);
                INTERVAL_CUR_FILE_WEEK_DATE.put(key, vo);
            } else {
                TaUsagePointDataWeek vo = new TaUsagePointDataWeek();
                vo.setPointId(pointId);
                vo.setDateShow(usageDateWeekMonNum);
                vo.setPointData(pointUsage);
                INTERVAL_CUR_FILE_WEEK_DATE.put(key, vo);
            }
        } else{
            //doIntervalWeekCsv();
            INTERVAL_CUR_READ_CSV_PATH_WEEK = filePath;
            //缓存清空后 记录新读的数据
            TaUsagePointDataWeek vo = new TaUsagePointDataWeek();
            vo.setPointId(pointId);
            vo.setDateShow(usageDateWeekMonNum);
            vo.setPointData(pointUsage);
            INTERVAL_CUR_FILE_WEEK_DATE.put(key, vo);
        }
    }
    //缓存中的数据 读入到数据库中 然后清空缓存
    public void doIntervalWeekCsv(){
            //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath不相同 说明是新读取的文件 要把之前内存中记录的数据插入数据库 然后清除缓存 重新计算
            for (Map.Entry<String, TaUsagePointDataWeek> map : INTERVAL_CUR_FILE_WEEK_DATE.entrySet()) {
                TaUsagePointDataWeek value = map.getValue();
                //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
                //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
                TaUsagePointDataWeek usagPointDateDateVo = taUsagePonitDataWeekMapper.getUsagePointDataWeekByPointIdAndTime(value.getPointId(), value.getDateShow());
                if (usagPointDateDateVo == null) {
                            /*    TaUsagePointDataWeek vo = new TaUsagePointDataWeek();
                                vo.setPointId(pointId);
                                vo.setDateShow(usageDateWeekMonNum);
                                vo.setPointData(pointUsage);*/
                    taUsagePonitDataWeekMapper.insertTaInstantPointDataWeek(value);
                } else {
                    double newResult = toolHelper.doubleSum(value.getPointData(), usagPointDateDateVo.getPointData());
                    value.setPointData(newResult);
                    taUsagePonitDataWeekMapper.updateTaPointDataWeekByPointIdAndTime(value);
                }
            }
            //循环完了 修改路径  要清掉缓存  很重要 很重要
            INTERVAL_CUR_FILE_WEEK_DATE.clear();
    }

    //用量 周 导出图
    public List<TaUsagePointDataWeek> queryUsagePointDataWeekSum(String startExpDate, String endExpDate, String pointIds) throws ParseException {
        int startExportTimeNum = Integer.parseInt(startExpDate);
        int endExportTimeNum = Integer.parseInt(endExpDate);
        return taUsagePonitDataWeekMapper.findUsagePointDataWeekByPointIdAndTime(startExportTimeNum,endExportTimeNum,pointIds);
    }


}
