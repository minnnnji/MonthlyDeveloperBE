package com.monthly_developer.monthly_developer_backend.repository;

import com.monthly_developer.monthly_developer_backend.model.counter.Counter;
import com.monthly_developer.monthly_developer_backend.model.post.RecruitingTeamPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CounterRepository extends MongoRepository<Counter, Integer>  {
    Counter findByType(String type);
}
