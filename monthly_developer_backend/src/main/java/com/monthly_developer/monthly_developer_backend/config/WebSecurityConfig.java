package com.monthly_developer.monthly_developer_backend.config;

import com.monthly_developer.monthly_developer_backend.advice.CustomAuthenticationEntryPoint;
import com.monthly_developer.monthly_developer_backend.service.TokenService;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import com.monthly_developer.monthly_developer_backend.token.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtToken jwtToken;
    private final TokenService tokenService;
    private final CustomAuthenticationEntryPoint CustomAuthenticationEntryPoint;

    public WebSecurityConfig(JwtToken jwtToken, TokenService tokenService, CustomAuthenticationEntryPoint CustomAuthenticationEntryPoint) {
        this.jwtToken = jwtToken;
        this.tokenService = tokenService;
        this.CustomAuthenticationEntryPoint = CustomAuthenticationEntryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable() // csrf disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() // 사용권한
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()// 권한 거부 시
                .exceptionHandling().authenticationEntryPoint(CustomAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtToken, tokenService),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가;


    }

}