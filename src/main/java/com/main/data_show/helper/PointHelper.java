package com.main.data_show.helper;

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
    public int savePoint(String pointName,String pointType,String unit,String blockNo,int operUserId){
        TaPoint pointByUserName = taPointService.findPointByPointName(pointName);
        if(pointByUserName==null){
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
            return taPointService.insert(point);
        }else{
            System.out.println("pointName:"+pointName+",已存在！");
            return pointByUserName.getPointId();
        }
    }
}