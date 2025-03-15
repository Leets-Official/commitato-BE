package com.leets.commitatobe.domain.user.dto.response;

import java.time.LocalDateTime;

public record UserCommitResponse(
	LocalDateTime commitDate,
	Integer cnt
) {
}
