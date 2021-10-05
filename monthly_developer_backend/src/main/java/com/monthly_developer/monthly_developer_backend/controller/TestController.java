package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.repository.RecruitingTeamPostRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private final RecruitingTeamPostRepository recruitingTeamPostRepository;

    public TestController(RecruitingTeamPostRepository recruitingTeamPostRepository) {
        this.recruitingTeamPostRepository = recruitingTeamPostRepository;
    }

    @GetMapping(value = "/login/oauth2/code/github")
    public void getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
    }

    @GetMapping(value = "/mongodb/findall")
    public void mongoDBFindAll(){
        System.out.println(recruitingTeamPostRepository.findAll());
    }

}