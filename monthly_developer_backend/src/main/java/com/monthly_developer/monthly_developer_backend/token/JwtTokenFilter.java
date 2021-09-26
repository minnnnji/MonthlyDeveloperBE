package com.monthly_developer.monthly_developer_backend.token;


import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.service.UserService;
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
    private final UserService userService;

    public JwtTokenFilter(JwtToken jwtToken, UserService userService) {
        this.jwtToken = jwtToken;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 탐색
        String token = resolveToken((HttpServletRequest) request);

        // 유효 토큰 확인
        if (token != null && jwtToken.validateToken(token)) {

            String userAudience = jwtToken.getUserInfo(token);
            User requestUser = userService.loadUserByUsername(userAudience);

            Authentication authentication = jwtToken.getAuthentication(requestUser);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("유효한 토큰입니다.");
        }
        chain.doFilter(request, response);
    }

    // Header에서 token 가져오기
    private String resolveToken(HttpServletRequest request) {
        return request.getHeader("AUTH-TOKEN");
    }
}