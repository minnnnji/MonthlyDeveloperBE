package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class AuthorityController {


    private final JwtToken jwtToken;
    private final UserRepository userRepository;

    public AuthorityController(JwtToken jwtToken, UserRepository userRepository) {
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    @PostMapping("/join")
    public void join(@RequestBody User user) {
        // 회원 가입은 기본 권한으로 가입
        userRepository.save(
                User.builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());

    }

    // 로그인
    @PostMapping("/login")
    public UserTokens login(@RequestBody Map<String, String> user) {

        User loginUser = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException(""));
        if (user.get("password").equals(loginUser.getPassword())) {
            return jwtToken.createAllTokens(loginUser.getEmail(), loginUser.getRoles());
        }
        else{
            UserTokens userTokens = new UserTokens();
            userTokens.setAccessToken("Fail!");
            userTokens.setRefreshToken("Fail!");
            return userTokens;
        }
    }

    @GetMapping("/validate")
    public void validate(@RequestBody Map<String, String> tokens){
        System.out.println("Access Token");
        System.out.println(jwtToken.validateToken(tokens.get("accessToken")));
        System.out.println(jwtToken.validateToken(tokens.get("refreshToken")));
    }
}
