package com.leets.commitatobe.domain.user.domain.repository;

import com.leets.commitatobe.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByGithubId(String githubId);
}