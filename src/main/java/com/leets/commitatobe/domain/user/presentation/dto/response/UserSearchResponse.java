package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserSearchResponse(
        String username,
        Integer exp,
        String tierName,
        Integer consecutiveCommitDays
) {
}
