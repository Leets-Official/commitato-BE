package com.leets.commitatobe.domain.user.dto;

public record UserRankResponse(
	String githubId,
	Integer exp,
	Integer consecutiveCommitDays,
	String tierName,
	Integer ranking
) {
}
