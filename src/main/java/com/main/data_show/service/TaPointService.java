package com.main.data_show.service;

import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaPointService {

    @Resource
    private TaPonitMapper taPonitMapper;

    public int insert(TaPoint taPoint){
        return taPonitMapper.insertTaPoint(taPoint);
    }

    public TaPoint findPointByPointName(String pointName){
        return taPonitMapper.findPointByPointName(pointName);
    }
}
