package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserRankResponse(
        String username,
        Integer exp,
        Integer consecutiveCommitDays,
        String tierName,
        Integer rank//랭킹 추가
) {
}
