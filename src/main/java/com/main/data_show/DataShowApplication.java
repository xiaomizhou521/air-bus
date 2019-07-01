package com.main.data_show;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataShowApplication {

    public static void main(String[] args) {
        System.out.println(12345);
        SpringApplication.run(DataShowApplication.class, args);
    }

}
