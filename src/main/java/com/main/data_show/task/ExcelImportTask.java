package com.main.data_show.task;

import com.main.data_show.helper.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
@Service
@PropertySource(value = "classpath:/application.properties")
public class ExcelImportTask {

    @Resource
    private CSVHelper csvHelper;

    @Scheduled(cron ="${cron}")
   public void importExcelStart() throws Exception {
        System.out.println("开始导入excel！！！！！！！！");
        System.out.println("开始导入excel！！！！！！！！");
        System.out.println("开始导入excel！！！！！！！！");
        System.out.println("开始导入excel！！！！！！！！");
        System.out.println("开始导入excel！！！！！！！！");
        csvHelper.startTimerImportCSVFile("");
    }

 /*    @Scheduled(fixedRate = 15000)
    public void exportExcelStart() throws Exception {
        System.out.println("------------------开始导出excel");
        throw new Exception("异常啦---------------");
    }*/
}
