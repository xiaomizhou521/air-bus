package com.main.data_show.task;

import com.main.data_show.helper.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
@Service
public class ExcelImportTask {

    @Resource
    private CSVHelper csvHelper;

   @Scheduled(fixedRate = 50000000)
  /* @Scheduled(cron = "0 30 2 * * ?")*/
   public void importExcelStart() throws Exception {
       System.out.println("开始导入excel");
       csvHelper.exportPointBaseData();
    }

 /*    @Scheduled(fixedRate = 15000)
    public void exportExcelStart() throws Exception {
        System.out.println("------------------开始导出excel");
        throw new Exception("异常啦---------------");
    }*/
}
