package com.main.data_show.mapper;

import com.main.data_show.pojo.TaImportCsvLogs;
import com.main.data_show.pojo.TaPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaImportCsvLogsMapper {

    public int insertLogs(TaImportCsvLogs taImportCsvLogs);

}
