package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.github.GithubAccessCode;
import com.monthly_developer.monthly_developer_backend.model.github.GithubUserInfo;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.service.OauthService;
import com.monthly_developer.monthly_developer_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class AuthorityController {

    private final UserService userService;
    private final OauthService oauthService;

    public AuthorityController(UserService userService, OauthService oauthService) {
        this.userService = userService;
        this.oauthService = oauthService;
    }

    // login or join end-point
    @PostMapping("/join")
    public ResponseEntity<ResponseMessage> join(@RequestBody GithubAccessCode githubAccessCode, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // get user information from Github
        GithubUserInfo user = oauthService.getUserInfo(githubAccessCode.getAccessCode());

        // validate and get token
        Map<String, Object> result = userService.joinUser(user.githubUserToUser());

        // response
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    // reissue token end-point
    @PostMapping("/reissue")
    public ResponseEntity<ResponseMessage> reissueToken(@RequestBody UserTokens userTokens, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // validate and reissue token
        Map<String, Object> result = userService.reissueUserToken(userTokens);

        // response
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // spring security test end-point
    @GetMapping("/user/test")
    public String userTest(){
        return "user!";
    }

}
