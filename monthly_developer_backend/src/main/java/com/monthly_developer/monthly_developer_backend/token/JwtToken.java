package com.monthly_developer.monthly_developer_backend.token;


import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtToken {

    private String secretKey = "null";

    private final UserService userDetailsService;

    public JwtToken(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public UserTokens createAllTokens(String userEmail, List<String> roles) {

        // 유효시간 1분으로 설정
        long accessTokenValidTime = 60 * 1000L;
        long refreshTokenValidTime = 120 * 1000L;

        Date now = new Date();

        UserTokens userTokens = new UserTokens();

        String accessToken = createToken(userEmail, roles, accessTokenValidTime, now).compact();
        String refreshToken = createToken(userEmail, roles, refreshTokenValidTime, now).compact();

        userTokens.setAccessToken(accessToken);
        userTokens.setRefreshToken(refreshToken);

        return userTokens;
    }

    private JwtBuilder createToken(String userEmail, List<String> roles, long accessTokenValidTime, Date now) {
        Claims claims = Jwts.claims();
        claims.setSubject(userEmail);
        claims.put("roles", roles);

        JwtBuilder newToken = Jwts.builder();
        newToken.setHeaderParam("typ", "JWT");

        newToken.setSubject(userEmail);
        newToken.setIssuedAt(now);
        newToken.setExpiration(new Date(now.getTime() + accessTokenValidTime));
        newToken.signWith(SignatureAlgorithm.HS256, secretKey);

        return newToken;
    }

    // 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Header에서 token 가져오기
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("AUTH-TOKEN");
    }

    // 토큰의 유효성 확인
    // 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
