package com.leets.commitatobe.domain.user.presentation.dto.response;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfoResponse(
        String githubId,
        String tierName,
        String characterUrl,
        Integer consecutiveCommitDays,
        Integer todayCommitCount,
        Integer totalCommitCount,
        List<Commit> commitList,
        LocalDateTime updatedAt
) {
    public static UserInfoResponse of(User user){
        return UserInfoResponse.builder()
                .githubId(user.getGithubId())
                .tierName(user.getTier().getTierName())
                .characterUrl(user.getTier().getCharacterUrl())
                .consecutiveCommitDays(user.getConsecutiveCommitDays())
                .todayCommitCount(user.getTodayCommitCount())
                .totalCommitCount(user.getTotalCommitCount())
                .commitList(user.getCommitList())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
