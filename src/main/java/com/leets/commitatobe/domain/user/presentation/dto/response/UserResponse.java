package com.leets.commitatobe.domain.user.presentation.dto.response;

public record UserResponse(
        //검색 기능 Dto
        String username,
        Integer exp,
        String tierName
) {

}
