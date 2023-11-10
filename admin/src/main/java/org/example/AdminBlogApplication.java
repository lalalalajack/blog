package org.example;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("org.example.dao")
@EnableSwagger2
public class AdminBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminBlogApplication.class,args);
    }
}
