package com.leets.commitatobe.domain.login.dto;

import lombok.Builder;

public class JwtDto {
    @Builder
    public record JwtResponse(
        String grantType,
        String accessToken,
        String refreshToken
    ){}

}
