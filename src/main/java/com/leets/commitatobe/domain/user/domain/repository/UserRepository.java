package com.leets.commitatobe.domain.user.domain.repository;

import com.leets.commitatobe.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

import com.leets.commitatobe.domain.user.presentation.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID>, UserQueryRepository{
    Optional<User> findByGithubId(String githubId);

    //경험치순으로 유저를 페이징하여 조회하는 메서드
    Page<User> findAllByOrderByExpDesc(Pageable pageable);

}