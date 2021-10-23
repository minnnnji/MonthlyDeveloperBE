package com.monthly_developer.monthly_developer_backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Test
    @DisplayName("Access, Refresh Token의 유효기간이 남아있을 때 재발급 관련")
    void reissueUserTokenTest(){
        //given
        User newUser = User.builder()
                .login("testLogin")
                .avatar("http://testAvatarURL")
                .email("test@test.com")
                .name("testName")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        Map<String, Object> join =  tokenService.joinUser(newUser);

        //when
        HashMap<String, Object> reissueUserToken = tokenService.reissueUserToken((UserTokens) join.get("data"));
        UserTokens userTokens = (UserTokens) reissueUserToken.get("data");

        //when
        assertNotNull(userTokens.getAccessToken());
        assertNotNull(userTokens.getRefreshToken());

    }


}