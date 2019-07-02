package com.main.data_show;

import cn.com.enorth.utility.Beans;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.main.data_show.mapper")
public class DataShowApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataShowApplication.class, args);
    }

}
