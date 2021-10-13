package com.monthly_developer.monthly_developer_backend.model.post;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "post")
@Builder
public class RecruitingTeamPost {
    @Id
    private int id;
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
