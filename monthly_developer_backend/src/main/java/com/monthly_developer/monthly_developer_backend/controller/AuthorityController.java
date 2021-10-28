package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.github.GithubAccessCode;
import com.monthly_developer.monthly_developer_backend.model.github.GithubUserInfo;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.service.OauthService;
import com.monthly_developer.monthly_developer_backend.service.TokenService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

// 인증과 관련된 작업을 하는 Controller
@RestController
public class AuthorityController {

    private final TokenService tokenService;
    private final OauthService oauthService;

    public AuthorityController(TokenService tokenService, OauthService oauthService) {
        this.tokenService = tokenService;
        this.oauthService = oauthService;
    }

    // 사용자가 로그인 혹은 회원가입 시 접근하게 되는 엔드 포인트
    @ApiOperation("oauth를 활용한 로그인")
    @PostMapping("/oauth")
    public ResponseEntity<ResponseMessage> join(@RequestBody GithubAccessCode githubAccessCode, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // 전달 받은 Github의 AccessCode를 토대로 정보 요청
        GithubUserInfo user = oauthService.getUserInfo(githubAccessCode.getAccessCode());

        // 서비스에서 활용할 토큰 생성
        Map<String, Object> result = tokenService.joinUser(user.githubUserToUser());

        // 결과 응답 내용 추가
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    // 토큰이 만료 되었을 때 갱신을 위한 엔드 포인트
    @ApiOperation("토큰 재발급")
    @PostMapping("/reissue-token")
    public ResponseEntity<ResponseMessage> reissueToken(@RequestBody UserTokens userTokens, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // 토큰 검증
        Map<String, Object> result = tokenService.reissueUserToken(userTokens);

        // 결과 응답 내용 추가
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }



}
