package com.main.data_show.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaPoint;
import com.main.data_show.pojo.TaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaPointService {

    @Resource
    private TaPonitMapper taPonitMapper;

    @Autowired
    private ToolHelper toolHelper;

    public int insert(TaPoint taPoint){
        return taPonitMapper.insertTaPoint(taPoint);
    }

    public void update(TaPoint taPoint){
         taPonitMapper.updateTaPointByPointID(taPoint);
    }

    public void updateTapointByName(TaPoint taPoint){
         taPonitMapper.updateTaPointByPointName(taPoint);
    }

    public List<TaPoint> getPointsByPage(Integer pageNo, Integer limit,String searchPointName,String searchRemarkName){
        PageHelper.startPage(pageNo,limit);
        List<TaPoint> pointsByPage = taPonitMapper.getPointsByPage(searchPointName,searchRemarkName,"");

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

    //取点的所有 路径
    public List<TaPoint> getAllPointRelativePath(){
        List<TaPoint> allPointRelativePath = taPonitMapper.getAllPointRelativePath();
        List<TaPoint> result  = new ArrayList<>();
        Set<String> checkSet = new HashSet<>();
        //去重
        for(TaPoint vo : allPointRelativePath){
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

}
