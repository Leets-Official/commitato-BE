package com.leets.commitatobe.domain.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private UUID id;

	@Column
	private String gitHubAccessToken;

	@Column
	private String username;

	@Column(nullable = false)
	private String githubId;

	@Column
	private String profileImage;

	@Column(nullable = false)
	@Builder.Default
	private Integer exp = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer consecutiveCommitDays = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer totalCommitCount = 0;

	@Column(nullable = false)
	@Builder.Default
	private Integer todayCommitCount = 0;

	@Column
	private Integer ranking;// 랭킹 추가

	@ManyToOne
	@JoinColumn(name = "tier_id")
	private Tier tier;

	@Column
	private LocalDateTime lastCommitUpdateTime;

	public void updateExp(Integer exp) {
		this.exp = exp;
	}

	public void updateTier(Tier tier) {
		this.tier = tier;
	}

	public void updateConsecutiveCommitDays(Integer consecutiveCommitDays) {
		this.consecutiveCommitDays = consecutiveCommitDays;
	}

	public void updateTotalCommitCount(Integer totalCommitCount) {
		this.totalCommitCount = totalCommitCount;
	}

	public void updateTodayCommitCount(Integer todayCommitCount) {
		this.todayCommitCount = todayCommitCount;
	}

	public void updateRank(Integer ranking) {
		this.ranking = ranking;
	}

	public void updateLastCommitUpdateTime(LocalDateTime lastCommitUpdateTime) {
		this.lastCommitUpdateTime = lastCommitUpdateTime;
	}

	public void updateGitHubAccessToken(String gitHubAccessToken) {
		this.gitHubAccessToken = gitHubAccessToken;
	}
}
