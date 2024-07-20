package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserResponse(
        RefreshTokenResponse refreshTokenResponse,
        GitHubAccessTokenResponse gitHubAccessTokenResponse
) {
    public record RefreshTokenResponse(
            String refreshToken
    ) {}

    public record GitHubAccessTokenResponse(
            String gitHubAccessToken
    ) {}
}

