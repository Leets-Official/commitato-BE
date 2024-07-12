package com.leets.commitatobe.domain.commit.domain.repository;

import com.leets.commitatobe.domain.commit.domain.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommitRepository extends JpaRepository<Commit, UUID> {
}