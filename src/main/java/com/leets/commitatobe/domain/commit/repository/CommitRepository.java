package com.leets.commitatobe.domain.commit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.user.domain.User;

public interface CommitRepository extends JpaRepository<Commit, UUID> {
	List<Commit> findAllByUser(User user);

	Optional<Commit> findByCommitDateAndUser(LocalDateTime commitDate, User user);

	List<Commit> findAllByUserOrderByCommitDateAsc(User user);

	@Query("SELECT c FROM commit c " +
		"WHERE c.user = :user " +
		"AND c.commitDate BETWEEN :startDate AND :endDate " +
		"ORDER BY c.commitDate")
	List<Commit> findCommitsByUser(
		@Param("user") User user,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate
	);
}
