package com.monthly_developer.monthly_developer_backend.token;


import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;


public class JwtToken {

    private final String secretKey;

    public JwtToken(String secretKey) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 토큰 생성
    public UserTokens createAllTokens(String userEmail, List<String> roles) {

        // 유효시간 1분으로 설정
        long accessTokenValidTime = 60 * 1000L;
        long refreshTokenValidTime = 120 * 1000L;

        Date now = new Date();

        UserTokens userTokens = new UserTokens();

        String accessToken = createToken(userEmail, false, roles, accessTokenValidTime, now);
        String refreshToken = createToken(userEmail, true, roles, refreshTokenValidTime, now);

        userTokens.setAccessToken(accessToken);
        userTokens.setRefreshToken(refreshToken);

        return userTokens;
    }

    private String createToken(String userLogin, boolean type, List<String> roles, long accessTokenValidTime, Date now) {

            JwtBuilder newToken = Jwts.builder();
            newToken.setHeaderParam("typ", "JWT");

            Claims claims = Jwts.claims();
            if (type){
                claims.setSubject("user_refresh_auth");
            }else{
                claims.setSubject("user_auth");
                claims.setAudience(userLogin);
            }

            claims.setIssuedAt(now);
            claims.setExpiration(new Date(now.getTime() + accessTokenValidTime));
            claims.put("roles", roles);
            newToken.setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey);

        return newToken.compact();
    }

    // 정보 조회
    public Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    // 정보 추출
    public String getUserInfo(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getAudience();
    }

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
