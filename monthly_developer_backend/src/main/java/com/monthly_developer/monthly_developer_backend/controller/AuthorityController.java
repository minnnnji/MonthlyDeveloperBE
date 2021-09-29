package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.service.UserService;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class AuthorityController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthorityController(UserService userService, JwtToken jwtToken, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/join/user")
    public ResponseEntity<ResponseMessage> join(@RequestBody User user, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        Map<String, Object> result = userService.joinUser(user);
        responseMessage.setRequestResult((String) result.get("result"));
        responseMessage.setData(result.get("data"));

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
