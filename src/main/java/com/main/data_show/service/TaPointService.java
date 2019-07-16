package com.main.data_show.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaPointService {

    @Resource
    private TaPonitMapper taPonitMapper;

    public int insert(TaPoint taPoint){
        return taPonitMapper.insertTaPoint(taPoint);
    }

    public void update(TaPoint taPoint){
         taPonitMapper.updateTaPointByPointID(taPoint);
    }

    public List<TaPoint> getPointsByPage(Integer pageNo, Integer limit){
        PageHelper.startPage(pageNo,limit);
        List<TaPoint> pointsByPage = taPonitMapper.getPointsByPage();

        return pointsByPage;
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

    public TaPoint findPointByPointId(int pointId){
        return taPonitMapper.findPointByPointId(pointId);
    }
}
