package com.leets.commitatobe.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leets.commitatobe.domain.login.service.LoginQueryService;
import com.leets.commitatobe.domain.user.dto.response.UserInfoResponse;
import com.leets.commitatobe.domain.user.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.dto.response.UserSearchResponse;
import com.leets.commitatobe.domain.user.service.UserQueryService;
import com.leets.commitatobe.global.response.ApiResponse;
import com.leets.commitatobe.global.response.CustomPageResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	private final UserQueryService userQueryService;
	private final LoginQueryService loginQueryService;

	@Operation(
		summary = "유저 정보 검색",
		description = "깃허브 아이디로 검색합니다."
	)
	@GetMapping("/search")
	public ApiResponse<UserSearchResponse> searchUsers(@RequestParam("githubId") String githubId) {
		return ApiResponse.onSuccess(userQueryService.searchUsersByGithubId(githubId));
	}

	@Operation(
		summary = "랭킹 조회",
		description = "경험치 순으로 유저 정보를 조회합니다."
	)
	@GetMapping("/ranking")
	public ApiResponse<CustomPageResponse<UserRankResponse>> getUsersByExp(@RequestParam(name = "page") int page,
		@RequestParam(name = "size") int size) {
		return ApiResponse.onSuccess(userQueryService.getUsersOrderByExp(page, size));
	}

	@GetMapping("/{githubId}")
	public ApiResponse<UserInfoResponse> getUserInfo(@PathVariable("githubId") String githubId) {
		String myGitHubId = loginQueryService.getGitHubId();
		return ApiResponse.onSuccess(userQueryService.findUserInfo(githubId, myGitHubId));
	}
}
