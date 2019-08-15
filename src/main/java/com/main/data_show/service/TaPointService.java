package com.main.data_show.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.main.data_show.consts.PointConst;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumPointReportTypeDefine;
import com.main.data_show.enums.EnumPointTypeDefine;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TaPointService {

    @Resource
    private TaPonitMapper taPonitMapper;

    @Autowired
    private ToolHelper toolHelper;

    public int insert(TaPoint taPoint){
        return taPonitMapper.insertTaPoint(taPoint);
    }

    public void delete(TaPoint taPoint){
         taPonitMapper.deleteTaPointByPointId(taPoint);
    }

    public void update(TaPoint taPoint){
         taPonitMapper.updateTaPointByPointID(taPoint);
    }

    public void updateTapointByName(TaPoint taPoint){
         taPonitMapper.updateTaPointByPointName(taPoint);
    }

    public List<TaPoint> getPointsByPage(int pageNo, int limit,String searchPointName,String searchRemarkName){
        PageHelper.startPage(pageNo,limit);
        //List<TaPoint> pointsByPage = taPonitMapper.getPointsByPage(searchPointName,searchRemarkName,"");
        pageNo = pageNo*limit;
        List<TaPoint> pointsByPage = taPonitMapper.getPointsByPageParam(searchPointName,searchRemarkName,"",pageNo,limit);

        return pointsByPage;
    }
   //从内存中取点数据 没没有才查
    public List<TaPoint> getIntervalPointsList(String pointType){
        if(EnumPointTypeDefine.instant.toString().equals(pointType)){
            if(SysConsts.INTERVAL_INSTANT_POINT_LIST==null){
                reloadIntervalPointsList();
            }
            return SysConsts.INTERVAL_INSTANT_POINT_LIST;
        }else if(EnumPointTypeDefine.usage.toString().equals(pointType)){
            if(SysConsts.INTERVAL_USAGE_POINT_LIST == null){
                reloadIntervalPointsList();
            }
            return SysConsts.INTERVAL_USAGE_POINT_LIST;
        }else{
            if(SysConsts.INTERVAL_ALL_POINT_MAP.isEmpty()){
                reloadIntervalPointsList();
            }
            List<TaPoint> pointList = new ArrayList<TaPoint>();
            for(Map.Entry<String,TaPoint> map : SysConsts.INTERVAL_ALL_POINT_MAP.entrySet()){
                pointList.add(map.getValue());
            }
            return pointList;
        }

    }

    public void reloadIntervalPointsList(){
      /*  SysConsts.INTERVAL_INSTANT_POINT_LIST = taPonitMapper.getPointsByPage("","", EnumPointTypeDefine.instant.toString());
        SysConsts.INTERVAL_USAGE_POINT_LIST = taPonitMapper.getPointsByPage("","", EnumPointTypeDefine.usage.toString());*/
        List<TaPoint> pointsByPage = taPonitMapper.getPointsByPage("", "", "");
        List<TaPoint> instantPointList = new ArrayList<TaPoint>();
        List<TaPoint> usagePointList = new ArrayList<TaPoint>();
        Map<String,TaPoint> resultMap = new HashMap<String,TaPoint>();
        for(TaPoint vo : pointsByPage){
              resultMap.put(vo.getPointName(),vo);
              if(EnumPointTypeDefine.instant.toString().equals(vo.getPointType())){
                  instantPointList.add(vo);
              }else if(EnumPointTypeDefine.usage.toString().equals(vo.getPointType())){
                  usagePointList.add(vo);
              }
        }
        SysConsts.INTERVAL_ALL_POINT_MAP = resultMap;
        SysConsts.INTERVAL_INSTANT_POINT_LIST = instantPointList;
        SysConsts.INTERVAL_USAGE_POINT_LIST = usagePointList;
    }

    public List<TaPoint> getPointsByPointIds(String pointIds){
        List<TaPoint> pointsByPage = taPonitMapper.getPointsByPointIds(pointIds);
        return pointsByPage;
    }

    public TaPoint findPointByPointName(String pointName){
        return taPonitMapper.findPointByPointName(pointName);
    }

    public List<TaPoint> likePointByPointName(String pointName){
        return taPonitMapper.likePointByPointName(pointName);
    }

    //取点的所有 路径
    public List<TaPoint> getAllPointRelativePath(){
      /*  List<TaPoint> allPointRelativePath = taPonitMapper.getAllPointRelativePath();*/
        //变成从缓存取
        List<TaPoint> result  = new ArrayList<>();
        Set<String> checkSet = new HashSet<>();
        //去重
        for(Map.Entry<String,TaPoint> map : SysConsts.INTERVAL_ALL_POINT_MAP.entrySet()){
            TaPoint vo = map.getValue();
            StringBuffer sb = new StringBuffer();
            sb.append(vo.getFileRelativePath()).append(vo.getFilePrefixName());
            String checkPath = sb.toString();
            if(!checkSet.contains(checkPath)){
                checkSet.add(checkPath);
                result.add(vo);
            }
        }
        return result;
    }

    public TaPoint findPointByPointId(int pointId){
        return taPonitMapper.findPointByPointId(pointId);
    }

    //取得固定的用于计算的点的信息
    public List<TaPoint> getAllSumTapointList(String pointReportType) throws Exception {
        List<TaPoint> taPointList = new ArrayList<TaPoint>();
        String[] split = null;
        if(EnumPointReportTypeDefine.dian.toString().equals(pointReportType)){
            split = PointConst.ALL_SUM_POINT_NAME_STR.split(",");
        }else if(EnumPointReportTypeDefine.hot.toString().equals(pointReportType)){
            split = PointConst.ALL_SUM_HOT_POINT_NAME_STR.split(",");
        }else if(EnumPointReportTypeDefine.cold.toString().equals(pointReportType)){
            split = PointConst.ALL_SUM_COLD_POINT_NAME_STR.split(",");
        }else{
            throw new Exception("错误的点报告类型");
        }
        for(String str : split){
            if(SysConsts.INTERVAL_ALL_POINT_MAP.containsKey(str)){
                taPointList.add(SysConsts.INTERVAL_ALL_POINT_MAP.get(str));
            }
        }
        return taPointList;
    }

}
