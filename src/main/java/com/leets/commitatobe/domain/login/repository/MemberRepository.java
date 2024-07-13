package com.leets.commitatobe.domain.login.repository;

import com.leets.commitatobe.domain.login.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByGithubId(String githubId);
}

