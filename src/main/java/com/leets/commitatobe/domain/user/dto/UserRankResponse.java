package com.leets.commitatobe.domain.user.dto;

public record UserRankResponse(
	String githubId,
	Integer exp,
	Integer consecutiveCommitDays,
	String tierName,
	Integer ranking//랭킹 추가
) {
}
