package com.leets.commitatobe.domain.user.usecase;


import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserSearchResponse;
import com.leets.commitatobe.global.response.CustomPageResponse;

public interface UserQueryService {
    UserSearchResponse searchUsersByGithubId(String GithubId);//유저 이름으로 유저 정보를 검색하는 메서드

    CustomPageResponse<UserRankResponse> getUsersOrderByExp(int page, int size);//경험치 순으로 페이징된 유저 정보를 조회

    String getUserGitHubAccessToken(String githubId);
}
