package com.monthly_developer.monthly_developer_backend.config;

import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public SetProperty setProperty(){
        return new SetProperty();
    }

    @Bean
    public JwtToken jwtToken(SetProperty setProperty){
        return new JwtToken(setProperty.getSecret_key());
    }

}
