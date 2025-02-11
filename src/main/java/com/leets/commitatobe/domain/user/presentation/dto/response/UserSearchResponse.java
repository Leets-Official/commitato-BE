package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserSearchResponse(
        Integer ranking,
        String githubId,
        String tierName,
        Integer exp,
        Integer consecutiveCommitDays
) {
}
