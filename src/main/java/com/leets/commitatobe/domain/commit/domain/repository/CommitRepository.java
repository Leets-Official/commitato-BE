package com.leets.commitatobe.domain.commit.domain.repository;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommitRepository extends JpaRepository<Commit, UUID> {
    List<Commit> findAllByUser(User user);

    Optional<Commit> findByCommitDateAndUser(LocalDateTime commitDate, User user);

    List<Commit> findAllByUserOrderByCommitDateAsc(User user);
}