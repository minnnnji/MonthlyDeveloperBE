package com.monthly_developer.monthly_developer_backend.token;

import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenTest {

    @Autowired
    JwtToken jwtToken;

    @Test
    @DisplayName("일반 권한을 가진 유저에 대한 토큰 생성")
    void createTokenTest(){
        //given
        String userEmail = "testLogin";
        List<String> roles = new ArrayList<>(){
            {
                add("ROLE_USER");
            }
        };

        // when
        UserTokens userTokens = jwtToken.createAllTokens(userEmail, roles);

        // then
        assertNotNull(userTokens.getAccessToken());
        assertNotNull(userTokens.getRefreshToken());
        assertEquals(jwtToken.getUserInfo(userTokens.getAccessToken()), "testLogin");
    }

}