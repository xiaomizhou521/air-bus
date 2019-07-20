package com.main.data_show;

import cn.com.enorth.utility.Beans;
import org.apache.logging.log4j.LogManager;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/*@SpringBootApplication*/
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
@ServletComponentScan
@EnableScheduling
@EnableConfigurationProperties
@ComponentScan
public class DataShowApplication {
    private static Logger logger = LoggerFactory.getLogger(DataShowApplication.class);
    public static void main(String[] args) {
        logger.info("----log4j记录开始-------");
        SpringApplication.run(DataShowApplication.class, args);
    }

}
