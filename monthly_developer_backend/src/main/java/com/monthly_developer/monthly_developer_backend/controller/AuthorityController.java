package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public void join(@RequestBody Map<String, String> user) {
        // 회원 가입은 기본 권한으로 가입
        userRepository.save(
                User.builder()
                        .email(user.get("email"))
                        .password(user.get("password"))
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build());

    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User loginUser = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException(""));
        if (user.get("password").equals(loginUser.getPassword())) {
            return jwtToken.createToken(loginUser.getEmail(), loginUser.getRoles());
        }
        else{
            return "Fail!";
        }
    }
}
