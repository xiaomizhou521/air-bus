package com.main.data_show.helper;

import com.main.data_show.pojo.TaImportCsvLogs;
import com.main.data_show.service.TaImportCsvLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImportCsvLogsHelper {

    @Autowired
    public TaImportCsvLogsService taImportCsvLogsService;

    //pointName 重复不存在的时候新增
    public void saveImportCsvLogs(String filePath,int state,String detail){
        TaImportCsvLogs log = new TaImportCsvLogs();
        log.setFilePath(filePath);
        log.setState(state);
        log.setDetail(detail);
        taImportCsvLogsService.insert(log);
    }
}
