package com.leets.commitatobe.domain.user.service;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.commitatobe.domain.login.service.LoginCommandService;
import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.dto.response.UserInfoResponse;
import com.leets.commitatobe.domain.user.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.dto.response.UserSearchResponse;
import com.leets.commitatobe.domain.user.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.CustomPageResponse;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
	private final UserRepository userRepository;
	private final LoginCommandService loginCommandService;

	@Transactional
	public UserSearchResponse searchUsersByGithubId(String githubId) {// 유저 이름으로 유저 정보 검색
		User user = userRepository.findByGithubId(githubId)
			.orElseThrow(() -> new ApiException(ErrorStatus._USER_NOT_FOUND));

		Tier tier = user.getTier();

		return new UserSearchResponse(
			user.getRanking(),
			user.getGithubId(),
			tier.getTierName(),
			user.getExp(),
			user.getConsecutiveCommitDays()
		);
	}

	public CustomPageResponse<UserRankResponse> getUsersOrderByExp(int page, int size) {//경험치 순으로 페이징된 유저 정보 조회
		Pageable pageable = PageRequest.of(page, size);
		Page<User> userRankingPage = userRepository.findAllByOrderByExpDesc(pageable);

		if (userRankingPage.isEmpty()) {
			return CustomPageResponse.from(Page.empty(pageable));  // 빈 페이지 반환
		}

		Page<UserRankResponse> userRankResponses = userRankingPage.map(user -> { // 각 사용자의 경험치 최신화 및 UserRankResponse 변환
			Tier tier = user.getTier();

			return new UserRankResponse(
				user.getGithubId(),
				user.getExp(),
				user.getConsecutiveCommitDays(),
				tier != null ? tier.getTierName() : "Unranked",
				user.getRanking());//랭킹 추가
		});

		return CustomPageResponse.from(userRankResponses);
	}

	public String getUserGitHubAccessToken(String githubId) {
		User user = userRepository.findByGithubId(githubId).orElseThrow(() -> new ApiException(_USER_NOT_FOUND));

		String gitHubAccessToken = user.getGitHubAccessToken();

		return loginCommandService.decrypt(gitHubAccessToken);
	}

	public UserInfoResponse findUserInfo(String githubId, String myGitHubId) {
		User user = userRepository.findByGithubId(githubId).orElseThrow(() -> new ApiException(_USER_NOT_FOUND));
		return UserInfoResponse.of(githubId.equals(myGitHubId), user);
	}
}
