package com.monthly_developer.monthly_developer_backend.token;


import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtToken {

    private String secretKey = "null";
    private final UserRepository userRepository;

    public JwtToken(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        JwtBuilder newToken = Jwts.builder();
        newToken.setHeaderParam("typ", "JWT");

        if (userEmail == null){

        }else {
            Claims claims = Jwts.claims();
            claims.setSubject("user_auth");
            claims.setAudience(userEmail);
            claims.setIssuedAt(now);
            claims.setExpiration(new Date(now.getTime() + accessTokenValidTime));
            claims.put("roles", roles);

            newToken.setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey);
        }

        return newToken;
    }

    // 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(getUserInfo(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없음"));
    }

    // 정보 추출
    public String getUserInfo(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getAudience();
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
