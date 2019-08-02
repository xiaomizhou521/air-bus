package com.main.data_show.service;

import com.github.pagehelper.PageHelper;
import com.main.data_show.consts.SysConsts;
import com.main.data_show.enums.EnumPointTypeDefine;
import com.main.data_show.helper.ToolHelper;
import com.main.data_show.mapper.TaImportCsvLogsMapper;
import com.main.data_show.mapper.TaPonitMapper;
import com.main.data_show.pojo.TaImportCsvLogs;
import com.main.data_show.pojo.TaPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaImportCsvLogsService {

    @Resource
    private TaImportCsvLogsMapper taImportCsvLogsMapper;

    @Autowired
    private ToolHelper toolHelper;

    public int insert(TaImportCsvLogs taImportCsvLogs){
        return taImportCsvLogsMapper.insertLogs(taImportCsvLogs);
    }


}
