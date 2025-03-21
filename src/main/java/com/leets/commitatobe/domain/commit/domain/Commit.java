package com.leets.commitatobe.domain.commit.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "commit")
@Getter
@NoArgsConstructor
public class Commit extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "commit_id")
	private UUID id;

	@Column
	private Integer cnt;

	@Column
	private LocalDateTime commitDate;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@Column(name = "is_calculated")
	private boolean isCalculated;//경험치 계산 여부를 나타낸다.

	public static Commit create(LocalDateTime commitDate, Integer cnt, User user) {
		return Commit.builder()
			.commitDate(commitDate)
			.cnt(cnt)
			.user(user)
			.build();
	}

	@Builder
	public Commit(LocalDateTime commitDate, Integer cnt, User user) {
		this.commitDate = commitDate;
		this.cnt = cnt;
		this.user = user;
	}

	public void updateCnt(Integer cnt) {
		if (!this.cnt.equals(cnt)) {
			this.cnt = cnt;
			markAsUncalculated();
		}
	}

	public void markAsCalculated() {
		isCalculated = true;
	}

	public void markAsUncalculated() {
		isCalculated = false;
	}

	public int calculateExp(int dailyBonusExp, int consecutiveDays, int bonusExpIncrease) {
		int bonusExp = dailyBonusExp + (consecutiveDays - 1) * bonusExpIncrease;
		return this.cnt * 5 + bonusExp;
	}

	public boolean commitDateIsToday() {
		return this.commitDate.toLocalDate().isEqual(LocalDate.now());
	}
}
