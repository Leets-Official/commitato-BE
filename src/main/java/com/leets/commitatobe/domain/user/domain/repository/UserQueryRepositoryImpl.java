package com.leets.commitatobe.domain.user.domain.repository;

import com.leets.commitatobe.domain.user.presentation.dto.response.UserInfoResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserInfoResponse findUserInfoByGithubId(String githubId) {
        return null;
    }
}
