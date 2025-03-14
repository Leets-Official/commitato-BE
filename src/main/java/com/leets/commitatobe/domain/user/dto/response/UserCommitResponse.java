package com.leets.commitatobe.domain.user.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record UserCommitResponse(
	LocalDateTime commitDate,
	Integer cnt
) {
}
