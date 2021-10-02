package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.GithubUserInfo;
import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.service.OauthService;
import com.monthly_developer.monthly_developer_backend.service.UserService;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
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

    @GetMapping("/join/user")
    public ResponseEntity<ResponseMessage> join(@RequestParam String accessCode, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());
        System.out.println(accessCode);
        GithubUserInfo user = oauthService.getUserInfo(accessCode);
        System.out.println(user);

        //Map<String, Object> result = userService.joinUser(user);
        //responseMessage.setRequestResult((String) result.get("result"));
        //responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseMessage> reissueToken(@RequestBody UserTokens userTokens, HttpServletRequest request) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        Map<String, Object> result = userService.reissueUserToken(userTokens);
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/user/test")
    public String userTest(){
        return "user!";
    }

}
