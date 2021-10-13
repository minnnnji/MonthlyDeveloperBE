package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.counter.Counter;
import com.monthly_developer.monthly_developer_backend.model.post.RecruitingTeamPost;
import com.monthly_developer.monthly_developer_backend.repository.CounterRepository;
import com.monthly_developer.monthly_developer_backend.repository.RecruitingTeamPostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    private final RecruitingTeamPostRepository recruitingTeamPostRepository;
    private final CounterRepository counterRepository;

    public TestController(RecruitingTeamPostRepository recruitingTeamPostRepository, CounterRepository counterRepository) {
        this.recruitingTeamPostRepository = recruitingTeamPostRepository;
        this.counterRepository = counterRepository;
    }

    // get github access code from github
    @GetMapping(value = "/login/oauth2/code/github")
    public void getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
    }

    // spring security test end-point
    @GetMapping("/user/test")
    public String userTest(){
        return "user!";
    }

    // find all data from mdb
    @GetMapping(value = "/mongodb/findall")
    public void mongoDBFindAll(){
        System.out.println(recruitingTeamPostRepository.findAll());
    }

    // write temp data to mdb
    @GetMapping(value = "/mongodb/write")
    public void mongoDBWrite(){
        Counter counter = getCounter("post_counter");
        RecruitingTeamPost recruitingTeamPost = RecruitingTeamPost.builder()
                .id(counter.getCounter() + 1)
                .title("test title")
                .writer("write test")
                .date("test")
                .build();
        updateCounter(counter);
        recruitingTeamPostRepository.save(recruitingTeamPost);
    }
    @GetMapping(value = "/mongodb/delete")
    public void mongoDBDelete(){
        recruitingTeamPostRepository.deleteById("write");
    }


    private Counter getCounter(String type){
        return counterRepository.findByType(type);
    }

    private void updateCounter(Counter counter){
        Counter updateCounter = Counter.builder()
                .type(counter.getType())
                ._id(counter.get_id())
                .counter(counter.getCounter() + 1)
                .build();
        counterRepository.save(updateCounter);
    }

    @GetMapping(value = "/exception")
    public void exception(){

        List<String> l = new ArrayList<>();
        System.out.println(l.get(1));
    }

}
