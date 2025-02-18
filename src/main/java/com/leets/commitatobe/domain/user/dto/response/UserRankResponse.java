package com.leets.commitatobe.domain.user.dto.response;

public record UserRankResponse(
	String githubId,
	Integer exp,
	Integer consecutiveCommitDays,
	String tierName,
	Integer ranking
) {
}
