package com.main.data_show;

import cn.com.enorth.utility.Beans;
import org.apache.logging.log4j.LogManager;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@MapperScan("com.main.data_show.mapper")
public class DataShowApplication {
    private static Logger logger = LoggerFactory.getLogger(DataShowApplication.class);
    public static void main(String[] args) {
        logger.info("----log4j记录开始-------");
        SpringApplication.run(DataShowApplication.class, args);
    }

}
