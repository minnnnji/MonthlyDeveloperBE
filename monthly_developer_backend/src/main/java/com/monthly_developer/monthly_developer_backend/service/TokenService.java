package com.monthly_developer.monthly_developer_backend.service;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TokenService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;

    private String _result = null;
    private Object _data = null;

    public TokenService(UserRepository userRepository, JwtToken jwtToken) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
    }

    public Map<String, Object> joinUser(User user){

        // 사용자의 별명을 토대로 DB에서 정보를 가져옴
        // 단, 사용자가 없을 경우 새로운 사용자로 추가
        User loginUser = userRepository.findByLogin(user.getLogin())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .login(user.getLogin())
                            .avatar(user.getAvatar())
                            .email(user.getEmail())
                            .name(user.getName())
                            .roles(Collections.singletonList("ROLE_USER"))
                            .build();

                    // 회원 가입은 기본 권한으로 가입
                    userRepository.save(newUser);

                    return newUser;
                });

        // 사용자의 별명이 동일한지 확인
        if (user.getLogin().equals(loginUser.getLogin())) {

            UserTokens _token = jwtToken.createAllTokens(loginUser.getLogin(), loginUser.getRoles());

            _result = "success";
            User newUser = User.builder()
                    .login(user.getLogin())
                    .avatar(user.getAvatar())
                    .email(user.getEmail())
                    .name(user.getName())
                    .roles(loginUser.getRoles())
                    .token(_token.getRefreshToken())
                    .build();

            userRepository.save(newUser);
            _data = _token;

        } else {
            _result = "fail";
            _data = null;
        }

        return new HashMap<String, Object>() {{
            put("result", _result);
            put("data", _data);
        }};
    }

    public HashMap<String, Object> reissueUserToken(UserTokens userTokens){


        /* Authentication 관련 참고 함수

        // ID
        System.out.println(token.getPrincipal());
        // PW
        System.out.println(token.getCredentials());
        // Detail
        System.out.println(token.getDetails());
        // Auth
        System.out.println(token.getAuthorities());
        // Name
        System.out.println(token.getName());

         */

        try {
            String userAudience = jwtToken.getUserInfo(userTokens.getAccessToken());
            User requestUser = loadUserByUsername(userAudience);
            Authentication token = jwtToken.getAuthentication(requestUser);

            _result = "fail";
            _data = "Access Token is not expired.";

            // 사인 오류
        } catch (SignatureException e) {
            _result = "fail";
            _data = "SignatureException";

            // 파싱 오류
        } catch (MalformedJwtException e) {
            _result = "fail";
            _data = "MalformedJwtException";

            // 타임 아웃
        } catch (ExpiredJwtException e) {

            try {
                String expiredUserMail = e.getClaims().getAudience();
                User requestUser = loadUserByUsername(expiredUserMail);

                if (requestUser.getToken().equals(userTokens.getRefreshToken())){
                    _result = "success";
                    _data = jwtToken.createAllTokens(requestUser.getLogin(), requestUser.getRoles());

                }else{
                    _result = "fail!";
                    _data = "Unknown Token!";
                }

            } catch (io.jsonwebtoken.SignatureException ie) {
                _result = "fail";
                _data = "SignatureException";

                // 파싱 오류
            } catch (io.jsonwebtoken.MalformedJwtException ie) {
                _result = "fail";
                _data = "MalformedJwtException";

                // 타임 아웃
            } catch (io.jsonwebtoken.ExpiredJwtException ie) {
                _result = "fail";
                _data = "All Token Time out!";
            }


        }

        return new HashMap<String, Object>() {{
            put("result", _result);
            put("data", _data);
        }};

    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없음"));
    }
}