package com.monthly_developer.monthly_developer_backend.repository;

import com.monthly_developer.monthly_developer_backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
