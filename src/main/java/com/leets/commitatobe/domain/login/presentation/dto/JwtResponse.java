package com.leets.commitatobe.domain.login.presentation.dto;


public record JwtResponse(
    String grantType,
    String accessToken,
    String refreshToken
) {}
