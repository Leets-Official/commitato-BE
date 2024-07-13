package com.leets.commitatobe.domain.login.dto;

import lombok.Builder;

public record GitHubDto() {

    @Builder
    public record UserDto(
        String userId
    ) {}

}
