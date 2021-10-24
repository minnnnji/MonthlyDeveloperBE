package com.monthly_developer.monthly_developer_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
// Swagger 관련 Config
class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(setApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.monthly_developer.monthly_developer_backend"))
                .build();
    }

    // api 에 대한 설명을 작성하는 곳
    public ApiInfo setApiInfo() {
        return new ApiInfoBuilder()
                .title("Monthly Developer API Doc.") // 제목
                .version("1.0") // 버전
                .description("Monthly Developer API") // 요약
                .build();
    }

}
