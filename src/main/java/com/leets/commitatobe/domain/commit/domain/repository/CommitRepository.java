package com.leets.commitatobe.domain.commit.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.user.domain.User;

public interface CommitRepository extends JpaRepository<Commit, UUID> {
	List<Commit> findAllByUser(User user);

	Optional<Commit> findByCommitDateAndUser(LocalDateTime commitDate, User user);

	List<Commit> findAllByUserOrderByCommitDateAsc(User user);
}
