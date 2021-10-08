package com.monthly_developer.monthly_developer_backend.repository;

import com.monthly_developer.monthly_developer_backend.model.post.RecruitingTeamPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecruitingTeamPostRepository extends MongoRepository<RecruitingTeamPost, String> {
    List<RecruitingTeamPost> findAll();

    void deleteById(String id);
}
