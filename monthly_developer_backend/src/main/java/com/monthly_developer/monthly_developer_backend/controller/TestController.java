package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.post.RecruitingTeamPost;
import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.repository.RecruitingTeamPostRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private final RecruitingTeamPostRepository recruitingTeamPostRepository;

    public TestController(RecruitingTeamPostRepository recruitingTeamPostRepository) {
        this.recruitingTeamPostRepository = recruitingTeamPostRepository;
    }

    // get github access code from github
    @GetMapping(value = "/login/oauth2/code/github")
    public void getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
    }

    // find all data from mdb
    @GetMapping(value = "/mongodb/findall")
    public void mongoDBFindAll(){
        System.out.println(recruitingTeamPostRepository.findAll());
    }

    // write temp data to mdb
    @GetMapping(value = "/mongodb/write")
    public void mongoDBWrite(){
        RecruitingTeamPost recruitingTeamPost = RecruitingTeamPost.builder()
                .id("write")
                .title("write test")
                .build();

        recruitingTeamPostRepository.save(recruitingTeamPost);
    }
    @GetMapping(value = "/mongodb/delete")
    public void mongoDBDelete(){
        recruitingTeamPostRepository.deleteById("write");
    }



}
