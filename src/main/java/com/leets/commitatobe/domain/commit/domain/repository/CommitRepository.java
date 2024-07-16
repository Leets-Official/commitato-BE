package com.leets.commitatobe.domain.commit.domain.repository;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommitRepository extends JpaRepository<Commit, UUID> {
    List<Commit> findAllByUser(User user);

    Optional<Commit> findByCommitDateAndUser(Date commitDate, User user);
}