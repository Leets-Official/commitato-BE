package com.leets.commitatobe.global.jwt.dto;

public record JwtResponse(
	String githubId,
	String grantType,
	String accessToken
) {
}
