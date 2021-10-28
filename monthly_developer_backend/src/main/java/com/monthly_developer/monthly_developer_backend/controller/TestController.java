package com.monthly_developer.monthly_developer_backend.controller;

import com.monthly_developer.monthly_developer_backend.model.ResponseMessage;
import com.monthly_developer.monthly_developer_backend.model.counter.Counter;
import com.monthly_developer.monthly_developer_backend.model.post.RecruitingTeamPost;
import com.monthly_developer.monthly_developer_backend.model.user.User;
import com.monthly_developer.monthly_developer_backend.model.user.UserTokens;
import com.monthly_developer.monthly_developer_backend.repository.CounterRepository;
import com.monthly_developer.monthly_developer_backend.repository.RecruitingTeamPostRepository;
import com.monthly_developer.monthly_developer_backend.repository.UserRepository;
import com.monthly_developer.monthly_developer_backend.token.JwtToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
    해당 Controller 는 새로운 기능 테스트, 적용을 위해 사용하는 Controller 임
*/

@RestController
@Api(description = "테스트를 위한 API 목록")
public class TestController {

    private final RecruitingTeamPostRepository recruitingTeamPostRepository;
    private final CounterRepository counterRepository;
    private final JwtToken jwtToken;
    private final UserRepository userRepository;

    public TestController(RecruitingTeamPostRepository recruitingTeamPostRepository, CounterRepository counterRepository, JwtToken jwtToken, UserRepository userRepository) {
        this.recruitingTeamPostRepository = recruitingTeamPostRepository;
        this.counterRepository = counterRepository;
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    // Github Oauth 테스트용
    // 리다이렉트를 통해 github로 부터 access code를 전달 받음
    @GetMapping(value = "/login/oauth2/code/github")
    public void getUserDataFromGithub(@RequestParam String code) {
        System.out.println(code);
    }

    // Spring Security 테스트용
    // 현재 Spring Security 에서 user URL에 대해 권한을 확인하고 있음
    // 권한에 따라 작동하는지 테스트 하기 위한 엔드포인트
    @GetMapping("/user/test")
    public String userTest(){
        return "user!";
    }

    // MongoDB 연동 테스트 용
    // MongoDB에서 게시글에 대한 모든 데이터를 가져오는 엔드 포인트
    @GetMapping(value = "/mongodb/findall")
    public void mongoDBFindAll(){
        System.out.println(recruitingTeamPostRepository.findAll());
    }

    // MongoDB 연동 테스트 용
    // MOngoDB 내에 새로운 게시글을 추가하는 엔드 포인트
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

    // MongoDB 연동 테스트 용
    // MOngoDB 내에 특정 데이터를 삭제하는 엔드 포인트
    @GetMapping(value = "/mongodb/delete")
    public void mongoDBDelete(){
        recruitingTeamPostRepository.deleteById("write");
    }



    // MongoDB 연동 테스트 용
    // 게시글 작성 중 게시글 번호를 부여하기 위해 Counter를 관리하는 메소드
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

    @PostMapping("/get-test-token")
    public ResponseEntity<ResponseMessage> getTestToken(@RequestBody User user, HttpServletRequest request) {

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setRequestPath(request.getRequestURI());

        // 전달 받은 Github의 AccessCode를 토대로 정보 요청
        User temp = userRepository.findByLogin(user.getLogin())
                .orElseGet(() -> {
                    return null;
                 });

        if (user.getLogin().equals(temp.getLogin())){

            UserTokens _token = jwtToken.createAllTokens(temp.getLogin(), Collections.singletonList("ROLE_USER"));

            User newUser = User.builder()
                    .login(temp.getLogin())
                    .avatar(temp.getAvatar())
                    .email(temp.getEmail())
                    .name(temp.getName())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .token(_token.getRefreshToken())
                    .build();

            userRepository.save(newUser);

            // 결과 응답 내용 추가
            responseMessage.setRequestResult("success");
            responseMessage.setData(_token);
        }else{
            responseMessage.setRequestResult("fail");
            responseMessage.setData(null);
        }


        return new ResponseEntity<>(responseMessage, HttpStatus.OK);


    }

    // 고의로 Exception 을 내는 엔드 포인트
    @GetMapping(value = "/exception")
    public void exception(){

        List<String> l = new ArrayList<>();
        System.out.println(l.get(1));
    }

}
