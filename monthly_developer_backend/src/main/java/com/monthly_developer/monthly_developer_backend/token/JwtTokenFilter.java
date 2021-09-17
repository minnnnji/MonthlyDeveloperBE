package com.monthly_developer.monthly_developer_backend.token;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtToken jwtToken;

    public JwtTokenFilter(JwtToken jwtToken) {
        System.out.println("JwtTokenFilter Service On!");
        this.jwtToken = jwtToken;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 탐색
        String token = jwtToken.resolveToken((HttpServletRequest) request);

        // 유효 토큰 확인
        if (token != null && jwtToken.validateToken(token)) {
            Authentication authentication = jwtToken.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("비어 있지 않은 유효한 토큰입니다.");
        }
        chain.doFilter(request, response);
    }
}