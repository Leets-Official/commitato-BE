package com.leets.commitatobe.domain.user.dto.response;

public record UserSearchResponse(
	Integer ranking,
	String githubId,
	String tierName,
	Integer exp,
	Integer consecutiveCommitDays
) {
}
