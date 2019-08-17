package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumUsageTimeTypeDefine;
import com.main.data_show.mapper.TaUsagePonitDataDateMapper;
import com.main.data_show.mapper.TaUsagePonitDataMapper;
import com.main.data_show.pojo.TaUsagePointData;
import com.main.data_show.pojo.TaUsagePointDataDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsagePointDataDateHelper {

    private static String INTERVAL_CUR_READ_CSV_PATH = "";

    public static Map<String,TaUsagePointDataDate> INTERVAL_CUR_FILE_DATA_DATE = new HashMap<String,TaUsagePointDataDate>();

    @Autowired
    public ToolHelper toolHelper;

    @Autowired
    public TaUsagePonitDataDateMapper taUsagePonitDataDateMapper;

    //先查询 如果存在 就相加 否则创建
    public void makeUsagePointDate(int pointId, double pointUsage, Date dateTime,String filePath) throws Exception {
            //先计算要放入数据库时间数字
          long usageDateWeekMonNum = toolHelper.getUsageDateWeekMonNum(dateTime, EnumUsageTimeTypeDefine.date.toString());
          //为了优化读取速度  在缓存里面计算日周月的   每个文件每个点每个时间只保存一次
           //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath相同  说明是同一个文件  在内存中记录数据
        String key = pointId + "@"+ usageDateWeekMonNum;
        if(INTERVAL_CUR_READ_CSV_PATH.equals(filePath)){
              if(INTERVAL_CUR_FILE_DATA_DATE.containsKey(key)){
                  TaUsagePointDataDate vo = INTERVAL_CUR_FILE_DATA_DATE.get(key);
                  double newResult = toolHelper.doubleSum(pointUsage, vo.getPointData());
                  vo.setPointData(newResult);
                  INTERVAL_CUR_FILE_DATA_DATE.put(key,vo);
              }else{
                  TaUsagePointDataDate vo = new TaUsagePointDataDate();
                  vo.setPointId(pointId);
                  vo.setDateShow(usageDateWeekMonNum);
                  vo.setPointData(pointUsage);
                  INTERVAL_CUR_FILE_DATA_DATE.put(key,vo);
              }
          }else{
             // doIntervalDateCsv();
            INTERVAL_CUR_READ_CSV_PATH = filePath;
            //清空缓存后 记录新读入的数据
              TaUsagePointDataDate vo = new TaUsagePointDataDate();
              vo.setPointId(pointId);
              vo.setDateShow(usageDateWeekMonNum);
              vo.setPointData(pointUsage);
              INTERVAL_CUR_FILE_DATA_DATE.put(key,vo);
          }
    }
    //缓存中的数据 读入到数据库中 然后清空缓存
    public void doIntervalDateCsv(){
        //如果内存中的INTERVAL_CUR_READ_CSV_PATH与filePath不相同 说明是新读取的文件 要把之前内存中记录的数据插入数据库 然后清除缓存 重新计算
        for(Map.Entry<String,TaUsagePointDataDate> map : INTERVAL_CUR_FILE_DATA_DATE.entrySet()){
            TaUsagePointDataDate value = map.getValue();
            //查询是否已经记录了这个时间，每个点的每个时间只会有一条数据
            TaUsagePointDataDate usagPointDateDateVo = taUsagePonitDataDateMapper.getUsagePointDataDateByPointIdAndTime(value.getPointId(), value.getDateShow());
            if(usagPointDateDateVo == null){
                                /*  TaUsagePointDataDate vo = new TaUsagePointDataDate();
                                  vo.setPointId(pointId);
                                  vo.setDateShow(usageDateWeekMonNum);
                                  vo.setPointData(pointUsage);*/
                taUsagePonitDataDateMapper.insertTaInstantPointDataDate(value);
            }else{
                double newResult = toolHelper.doubleSum(value.getPointData(), usagPointDateDateVo.getPointData());
                value.setPointData(newResult);
                taUsagePonitDataDateMapper.updateTaPointDataDateByPointIdAndTime(value);
            }
        }
        //循环完了 修改路径  要清掉缓存  很重要 很重要
        INTERVAL_CUR_FILE_DATA_DATE.clear();
    }

    //用量 日 导出图
    public List<TaUsagePointDataDate> queryUsagePointDataSum(String startExpDate, String endExpDate,String pointIds) throws ParseException {
        Date startExp = toolHelper.makeStrToDate(startExpDate, SysConsts.DATE_FORMAT_7);
        Date endExp = toolHelper.makeStrToDate(endExpDate, SysConsts.DATE_FORMAT_7);
        //单个取每个点的数据
        long startExportTimeNum = toolHelper.dateToNumDate(startExp, SysConsts.DATE_FORMAT_4);
        long endExportTimeNum = toolHelper.dateToNumDate(endExp, SysConsts.DATE_FORMAT_4);
        return taUsagePonitDataDateMapper.findUsagePointDataDateByPointIdAndTime(startExportTimeNum,endExportTimeNum,pointIds);
    }


}
