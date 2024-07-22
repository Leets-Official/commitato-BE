package com.leets.commitatobe.domain.user.usecase;

import com.leets.commitatobe.domain.commit.usecase.ExpService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserResponse;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ExpService expService;
    @Override
    @Transactional
    public UserResponse searchUsersByGithubId(String githubId) {// 유저 이름으로 유저 정보 검색
        User user=userRepository.findByGithubId(githubId)
                .orElseThrow(()->new ApiException(ErrorStatus._USER_NOT_FOUND));
        expService.calculateAndSaveExp(user.getGithubId());
        return new UserResponse(
                user.getUsername(),
                user.getExp(),
                user.getTier()!=null?user.getTier().getTierName():"Unranked",
                user.getConsecutiveCommitDays()
        );
    }
    @Override
    public Page<UserRankResponse> getUsersByExp(Pageable pageable){//경험치 순으로 페이징된 유저 정보 조회
        Page<User> userPage= userRepository.findAllByOrderByExpDesc(pageable);

        return userPage.map(user->{// 각 사용자의 경험치 최신화 및 UserRankResponse 변환
            expService.calculateAndSaveExp(user.getGithubId());// 경험치 계산 및 저장
            return new UserRankResponse(
                    user.getUsername(),
                    user.getExp(),
                    user.getConsecutiveCommitDays(),
                    user.getTier()!=null?user.getTier().getTierName():"Unranked");
        });
    }
}