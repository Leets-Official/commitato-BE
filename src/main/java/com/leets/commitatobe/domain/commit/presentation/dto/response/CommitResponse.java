package com.leets.commitatobe.domain.commit.presentation.dto.response;

import com.leets.commitatobe.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CommitResponse(
        Boolean isMyAccount,
        String githubId,
        Integer exp,
        String tierName,
        String characterUrl,
        Integer consecutiveCommitDays,
        Integer todayCommitCount,
        Integer totalCommitCount,
        LocalDateTime updatedAt
) {
    public static CommitResponse of(boolean isMyAccount, User user){
        return CommitResponse.builder()
                .isMyAccount(isMyAccount)
                .githubId(user.getGithubId())
                .exp(user.getExp())
                .tierName(user.getTier().getTierName())
                .characterUrl(user.getTier().getCharacterUrl())
                .consecutiveCommitDays(user.getConsecutiveCommitDays())
                .todayCommitCount(user.getTodayCommitCount())
                .totalCommitCount(user.getTotalCommitCount())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
