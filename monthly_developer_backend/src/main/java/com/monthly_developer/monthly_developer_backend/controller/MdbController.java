package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.service.MdbService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MdbController {

    private final MdbService mdbService;

    public MdbController(MdbService mdbService) {
        this.mdbService = mdbService;
    }


    @GetMapping(value = "/login/oauth2/code/github")
    public String getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
        mdbService.getUserInfo(code);
        return "test123";
    }


}
