package com.main.data_show.service;

import com.main.data_show.mapper.TaPonitDataMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaPointData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TaPointDataService {

    @Resource
    private TaPonitDataMapper taPonitDataMapper;

    public int insert(TaPointData taPointData){
        return taPonitDataMapper.insertTaPointData(taPointData);
    }

}
