package com.monthly_developer.monthly_developer_backend.model.post;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "post")
public class RecruitingTeamPost {
    @Id
    private String id;
    private String title;
    private String writer;
    private String date;

    @Override
    public String toString() {
        return "RecruitingTeamPost{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
