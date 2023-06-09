package org.example;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.dao")
public class AdminBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminBlogApplication.class,args);
    }
}
