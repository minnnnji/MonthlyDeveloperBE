package com.monthly_developer.monthly_developer_backend.token;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Collections;
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

    @Test
    @DisplayName("전달 받은 User 에 대한 정보 조회")
    void getAuthenticationTest(){
        //given
        User user = User.builder()
                .login("testLogin")
                .avatar("http://testAvatarURL")
                .email("test@test.com")
                .name("testName")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        // when
        Authentication authentication = jwtToken.getAuthentication(user);

        // then
        assertNotNull(authentication);
        assertEquals(authentication.getPrincipal(), user);
        assertEquals(authentication.getCredentials(), "");
        assertEquals(authentication.getAuthorities(), user.getAuthorities());
    }

    @Test
    @DisplayName("전달 받은 Token에서 Audience를 추출")
    void getUserInfoTest(){
        //given
        String userEmail = "testLogin";
        List<String> roles = new ArrayList<>(){
            {
                add("ROLE_USER");
            }
        };
        UserTokens userTokens = jwtToken.createAllTokens(userEmail, roles);

        // when
        String userAudience = jwtToken.getUserInfo(userTokens.getAccessToken());

        // then
        assertNotNull(userAudience);
        assertEquals(userAudience, userEmail);
    }

    @Test
    @DisplayName("Token 만료 확인")
    void validateTokenTest(){
        //given
        String userEmail = "testLogin";
        List<String> roles = new ArrayList<>(){
            {
                add("ROLE_USER");
            }
        };
        UserTokens userTokens = jwtToken.createAllTokens(userEmail, roles);

        // when
        boolean isExpired = jwtToken.validateToken(userTokens.getAccessToken());

        // then
        assertTrue(isExpired);
    }

}