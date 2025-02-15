package com.leets.commitatobe.global.jwt.dto;

public record JwtResponse(
	String grantType,
	String accessToken
) {
}