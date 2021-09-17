package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.service.MdbService;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class OauthController {

    private final MdbService mdbService;


    public OauthController(MdbService mdbService) {
        this.mdbService = mdbService;

    }

    @GetMapping(value = "/login/oauth2/code/github")
    public String getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
        mdbService.getUserInfo(code);
        return "test123";
    }

    //





}
