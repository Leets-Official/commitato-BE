package com.leets.commitatobe.domain.user.dto;

public record UserResponse(
	GitHubAccessTokenResponse gitHubAccessTokenResponse
) {
	public record GitHubAccessTokenResponse(
		String gitHubAccessToken
	) {
	}
}
