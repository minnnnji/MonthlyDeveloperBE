package com.monthly_developer.monthly_developer_backend.model.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.monthly_developer.monthly_developer_backend.model.user.User;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubUserInfo {

    private String login;
    private String avatar;
    private String email;
    private String name;

    @Override
    public String toString() {
        return "GithubUserInfo{" +
                "avatarUrl='" + avatar + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User githubUserToUser(){
        return User.builder()
                .login(getLogin())
                .avatar(getAvatar())
                .email(getEmail())
                .name(getName())
                .build();
    }
}
