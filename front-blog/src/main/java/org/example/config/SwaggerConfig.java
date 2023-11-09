package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("cwj", "https://gitee.com/lil_wayne_chen", "1875576725@qq.com");
        return new ApiInfoBuilder()
                .title("前台")
                .description("博客前台系统文档")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}