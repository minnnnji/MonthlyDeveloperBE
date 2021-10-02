package com.monthly_developer.monthly_developer_backend.service;

import com.monthly_developer.monthly_developer_backend.config.SetProperty;
import com.monthly_developer.monthly_developer_backend.model.GithubAccessToken;
import com.monthly_developer.monthly_developer_backend.model.GithubUserInfo;
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

    public GithubUserInfo getUserInfo(String code){

        GithubAccessToken githubAccessToken = getAccessToken(code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + githubAccessToken.getAccessToken());

        HttpEntity httpEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<GithubUserInfo> res = restTemplate.exchange("https://api.github.com/user",
                HttpMethod.GET,
                httpEntity,
                GithubUserInfo.class);

        return res.getBody();

    }

    private GithubAccessToken getAccessToken(String code){

        GithubAccessToken token = new GithubAccessToken();

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", setProperty.getClient_id());
        param.add("client_secret", setProperty.getClient_secret());
        param.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        HttpEntity httpEntity = new HttpEntity<>(param, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GithubAccessToken> res = restTemplate.exchange("https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                httpEntity,
                GithubAccessToken.class);

        token = res.getBody();

        return token;
    }


}
