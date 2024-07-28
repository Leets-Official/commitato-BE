package com.leets.commitatobe.domain.user.presentation.dto.response;

import java.util.UUID;

public record UserRankResponse(
        String username,
        Integer exp,
        Integer consecutiveCommitDays,
        String tierName,
        Integer rank//랭킹 추가
) {
}
