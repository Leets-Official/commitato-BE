package com.leets.commitatobe.domain.user.dto.response;

import java.time.LocalDateTime;

import com.leets.commitatobe.domain.commit.domain.Commit;

public record UserCommitResponse(
	LocalDateTime commitDate,
	Integer cnt
) {
	public static UserCommitResponse of(Commit commit) {
		return new UserCommitResponse(commit.getCommitDate(), commit.getCnt());
	}
}
