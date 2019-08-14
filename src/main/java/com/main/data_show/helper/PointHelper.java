package com.main.data_show.helper;

import com.main.data_show.consts.SysConsts;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.service.TaPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PointHelper {

    @Autowired
    public TaPointService taPointService;

    //pointName 重复不存在的时候新增
    public TaPoint savePoint(String pointName,String pointType,String unit,String blockNo,int operUserId,String relativePath,String fileNamePrefix){
        //从内存中拿 如果不存在 创建  放入内存一份
        if(SysConsts.INTERVAL_ALL_POINT_MAP.containsKey(pointName)){
            System.out.println("pointName:"+pointName+",已存在！");
            return SysConsts.INTERVAL_ALL_POINT_MAP.get(pointName);
        }else{
                      /*  TaPoint pointByUserName = taPointService.findPointByPointName(pointName);
                        if(pointByUserName==null){*/
            //测试插入taPoint
            TaPoint point = new TaPoint();
            point.setPointName(pointName);
            point.setPointType(pointType);
            point.setPointUnit(unit);
            point.setRemarksName(unit);
            point .setBlockNo(blockNo);
            point.setState(1);
            point.setInitDate(new Date());
            point.setModDate(new Date());
            point.setInitUser(operUserId);
            point.setModUser(operUserId);
            point.setFileRelativePath(relativePath);
            point.setFilePrefixName(fileNamePrefix);
            int newPointid = taPointService.insert(point);
            TaPoint pointByUserNamenew = taPointService.findPointByPointName(pointName);
            //放入内存
            SysConsts.INTERVAL_ALL_POINT_MAP.put(pointByUserNamenew.getPointName(),pointByUserNamenew);
            return pointByUserNamenew;
        }
    }

    //通过点的名字得到id
    public int getPointIdByPointName(String pointName){
        TaPoint point = SysConsts.INTERVAL_ALL_POINT_MAP.get(pointName);
        return point.getPointId();
    }
}
