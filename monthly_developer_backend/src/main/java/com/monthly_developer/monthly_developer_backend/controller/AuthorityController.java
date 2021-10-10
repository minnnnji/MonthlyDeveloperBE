package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.github.GithubAccessCode;
import com.monthly_developer.monthly_developer_backend.model.github.GithubUserInfo;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.service.OauthService;
import com.monthly_developer.monthly_developer_backend.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AuthorityController {

    private final TokenService tokenService;
    private final OauthService oauthService;

    public AuthorityController(TokenService tokenService, OauthService oauthService) {
        this.tokenService = tokenService;
        this.oauthService = oauthService;
    }

    // login or join end-point
    @PostMapping("/oauth")
    public ResponseEntity<ResponseMessage> join(@RequestBody GithubAccessCode githubAccessCode, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // get user information from Github
        GithubUserInfo user = oauthService.getUserInfo(githubAccessCode.getAccessCode());

        // validate and get token
        Map<String, Object> result = tokenService.joinUser(user.githubUserToUser());

        // response
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    // reissue token end-point
    @PostMapping("/reissue-token")
    public ResponseEntity<ResponseMessage> reissueToken(@RequestBody UserTokens userTokens, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // validate and reissue token
        Map<String, Object> result = tokenService.reissueUserToken(userTokens);

        // response
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }



}
