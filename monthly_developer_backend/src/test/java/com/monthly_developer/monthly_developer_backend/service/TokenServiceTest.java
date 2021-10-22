package com.monthly_developer.monthly_developer_backend.service;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("신규 유저가 회원가입, 이 후 토큰 발급")
    void joinNewUserTest(){
        //given
        User newUser = User.builder()
                .login("testLogin")
                .avatar("http://testAvatarURL")
                .email("test@test.com")
                .name("testName")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        //when
        Map<String, Object> join =  tokenService.joinUser(newUser);

        //then
        assertEquals(join.get("result"), "success");
        assertNotNull(join.get("data"));

    }


}