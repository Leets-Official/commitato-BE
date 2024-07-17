package com.leets.commitatobe.domain.user.usecase;


import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserResponse;
import com.leets.commitatobe.global.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserQueryService {
    List<UserResponse> searchUsersByUsername(String username) throws ApiException;//유저 이름으로 유저 정보를 검색하는 메서드
    Page<UserRankResponse> getUsersByExp(Pageable pageable);//경험치 순으로 페이징된 유저 정보를 조회
}