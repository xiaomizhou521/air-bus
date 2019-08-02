package com.main.data_show.mapper;

import com.main.data_show.pojo.TaImportCsvLogs;
import com.main.data_show.pojo.TaPoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaImportCsvLogsMapper {

    public int insertLogs(TaImportCsvLogs taImportCsvLogs);
    public int getReadCsvLogByPageParam(TaImportCsvLogs taImportCsvLogs);
    List<TaImportCsvLogs> getReadCsvLogByPageParam(@Param("file_path")String file_path,@Param("state")String state,@Param("startExpDate")String startExpDate,@Param("endExpDate")String endExpDate,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

}
