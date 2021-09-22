package com.monthly_developer.monthly_developer_backend.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/join/user")
    public ResponseEntity<ResponseMessage> join(@RequestBody User user, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        User loginUser = userRepository.findByEmail(user.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .roles(Collections.singletonList("ROLE_USER"))
                            .build();

                    // 회원 가입은 기본 권한으로 가입
                    userRepository.save(newUser);

                    return newUser;
                });

        if (user.getPassword().equals(loginUser.getPassword())) {

            responseMessage.setRequestResult("success");
            responseMessage.setData(jwtToken.createAllTokens(loginUser.getEmail(), loginUser.getRoles()));

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } else {
            responseMessage.setRequestResult("fail");
            responseMessage.setData(null);

            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }


    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseMessage> reissueToken(@RequestBody UserTokens userTokens, HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        //        // ID
        //        System.out.println(token.getPrincipal());
        //        // PW
        //        System.out.println(token.getCredentials());
        //        // Detail
        //        System.out.println(token.getDetails());
        //        // Auth
        //        System.out.println(token.getAuthorities());
        //
        //        System.out.println();
        //
        //        System.out.println(token.getName());

        // 유효한 토큰 확인
        try {
            Authentication token = jwtToken.getAuthentication(userTokens.getAccessToken());
            responseMessage.setRequestResult("fail");
            responseMessage.setData("Access Token is not expired.");
            // 사인 오류
        } catch (io.jsonwebtoken.SignatureException e) {
            responseMessage.setRequestResult("fail");
            responseMessage.setData("SignatureException");
            // 파싱 오류
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            responseMessage.setRequestResult("fail");
            responseMessage.setData("MalformedJwtException");
            // 타임 아웃
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            responseMessage.setRequestResult("success");
            responseMessage.setData("Expired!");
        }


        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    @GetMapping("/validate")
    public void validate(@RequestBody Map<String, String> tokens) {
        System.out.println("Access Token");
        System.out.println(jwtToken.validateToken(tokens.get("accessToken")));
        System.out.println(jwtToken.validateToken(tokens.get("refreshToken")));

        try {
            System.out.println(jwtToken.getUserInfo(tokens.get("accessToken")));
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("검증 오류!");
        }

    }
}
