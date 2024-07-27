package com.leets.commitatobe.domain.user.domain.repository;

import com.leets.commitatobe.domain.user.presentation.dto.response.UserInfoResponse;

public interface UserQueryRepository{
    UserInfoResponse findUserInfoByGithubId(String githubId);

}
