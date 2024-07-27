package com.leets.commitatobe.domain.user.presentation.dto.response;

import java.time.LocalDateTime;

public record UserInfoResponse(
        String githubId,
        String tierName,
        String characterUrl,
        Integer consecutiveCommitDays,
        Integer todayCommitCount,
        Integer totalCommitCount,
        LocalDateTime updatedAt
) {
}
