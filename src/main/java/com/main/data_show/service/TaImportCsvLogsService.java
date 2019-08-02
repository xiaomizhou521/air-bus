package com.main.data_show.service;

import com.github.pagehelper.PageHelper;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumPointTypeDefine;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaImportCsvLogsMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaImportCsvLogs;
import com.main.data_show.pojo.TaPoint;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TaImportCsvLogsService {

    @Resource
    private TaImportCsvLogsMapper taImportCsvLogsMapper;

    @Autowired
    private ToolHelper toolHelper;

    public int insert(TaImportCsvLogs taImportCsvLogs){
        return taImportCsvLogsMapper.insertLogs(taImportCsvLogs);
    }

    public List<TaImportCsvLogs> getReadCsvLogByPageParam(String file_path,String state,String startTime,String endTime,int pageNo, int limit){
        PageHelper.startPage(pageNo,limit);
        pageNo = pageNo*limit;
        return taImportCsvLogsMapper.getReadCsvLogByPageParam(file_path,state,startTime,endTime,pageNo,limit);
    }


}
