package com.leets.commitatobe.domain.login.dto;

import lombok.Builder;

public record JwtDto() {
    @Builder
    public record JwtResponse(
        String grantType,
        String accessToken,
        String refreshToken
    ){}

}
