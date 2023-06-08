package org.example;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.example.dao")
@EnableScheduling
public class FrontBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontBlogApplication.class,args);
    }
}
