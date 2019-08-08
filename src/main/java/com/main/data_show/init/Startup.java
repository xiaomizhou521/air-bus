package com.main.data_show.init;

import com.main.data_show.helper.CSVHelper;
import com.main.data_show.service.TaPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Startup implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(Startup.class);
    @Autowired
    private TaPointService taPointService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("------------------------初始化point缓存开始-------------------------------");
        taPointService.reloadIntervalPointsList();
        logger.info("------------------------初始化point缓存结束-------------------------------");
    }
}
