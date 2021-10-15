package com.monthly_developer.monthly_developer_backend.service;

import com.monthly_developer.monthly_developer_backend.config.SetProperty;
import com.monthly_developer.monthly_developer_backend.model.github.GithubAccessToken;
import com.monthly_developer.monthly_developer_backend.model.github.GithubUserInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthService {

    private final SetProperty setProperty;

    public OauthService(SetProperty setProperty) {
        this.setProperty = setProperty;
    }

    // 전달받은 AccessCode를 Github으로 보내 사용자 정보를 가져오는 메소드
    // github 닉네임, 이메일, 이름, 프로필 사진을 가져온다.
    public GithubUserInfo getUserInfo(String accessCode){

        // 정보를 받아오기 위해서는 AccessCode로 AccessToken을 받급받음
        GithubAccessToken githubAccessToken = getAccessToken(accessCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + githubAccessToken.getAccessToken());

        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GithubUserInfo> res = restTemplate.exchange("https://api.github.com/user",
                HttpMethod.GET,
                httpEntity,
                GithubUserInfo.class);

        return res.getBody();

    }

    // AccessCode로 AccessToken을 받급 받는 메소드
    private GithubAccessToken getAccessToken(String accessCode){

        GithubAccessToken token = new GithubAccessToken();

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", setProperty.getClient_id());
        param.add("client_secret", setProperty.getClient_secret());
        param.add("code", accessCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(param, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GithubAccessToken> res = restTemplate.exchange("https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                httpEntity,
                GithubAccessToken.class);

        token = res.getBody();

        return token;
    }


}
