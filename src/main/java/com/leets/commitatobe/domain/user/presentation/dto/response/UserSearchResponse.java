package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserSearchResponse(
        String username,
        Integer exp,
        String tierName,
        String characterUrl,
        String badgeUrl,
        Integer consecutiveCommitDays
) {
}
