package com.sea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.sea.dao")
public class CampusContext {
    public static void main(String[] args) {
        SpringApplication.run(CampusContext.class, args);
    }
}