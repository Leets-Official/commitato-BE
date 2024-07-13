package com.leets.commitatobe.domain.login.dto;


public record JwtResponse(
    String grantType,
    String accessToken,
    String refreshToken
) {}
