package com.monthly_developer.monthly_developer_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public SetProperty setProperty(){
        return new SetProperty();
    }
}
