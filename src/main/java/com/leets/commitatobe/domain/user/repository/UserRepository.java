package com.leets.commitatobe.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.commitatobe.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByGithubId(String githubId);

	//경험치순으로 유저를 페이징하여 조회하는 메서드
	Page<User> findAllByOrderByExpDesc(Pageable pageable);

}
