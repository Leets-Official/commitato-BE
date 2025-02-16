package com.leets.commitatobe.domain.commit.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leets.commitatobe.domain.commit.presentation.dto.response.CommitResponse;
import com.leets.commitatobe.domain.commit.usecase.FetchCommits;
import com.leets.commitatobe.domain.commit.usecase.FetchCommitsTest;
import com.leets.commitatobe.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Commit 컨트롤러", description = "GitHub에서 사용자의 Commit 정보를 호출하고 데이터 가공을 처리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/commit")
public class CommitController {
	private final FetchCommits fetchCommits;
	private final FetchCommitsTest fetchCommitsTest;

	@Operation(
		summary = "커밋 기록 업데이트",
		description = "GitHub에서 커밋 기록을 가져와 DB에 저장하고 사용자의 정보를 최신화 합니다.")
	@PostMapping("/update")
	public ApiResponse<CommitResponse> fetchCommits() {
		return ApiResponse.onSuccess(fetchCommits.execute());
	}

	@Operation(
		summary = "커밋 기록 업데이트 (테스트)",
		description = "테스트를 위해, 7월 1일부터 커밋 기록을 가져와 DB에 저장하고 사용자의 정보를 최신화 합니다.")
	@PostMapping("update/test")
	public ApiResponse<CommitResponse> fetchCommitsTest() {
		return ApiResponse.onSuccess(fetchCommitsTest.execute());
	}
}
