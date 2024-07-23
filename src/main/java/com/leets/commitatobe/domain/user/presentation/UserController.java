package com.leets.commitatobe.domain.user.presentation;

import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserSearchResponse;
import com.leets.commitatobe.domain.user.usecase.UserQueryService;
import com.leets.commitatobe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserQueryService userQueryService;
    @Operation(
            summary = "유저 정보 검색",
            description = "깃허브 아이디로 검색합니다."
    )
    @GetMapping("/search")
    public ApiResponse<UserSearchResponse> searchUsers(@RequestParam("githubId")String githubId){
        return ApiResponse.onSuccess(userQueryService.searchUsersByGithubId(githubId));
    }
    @GetMapping("/ranking")//경험치 순으로 유저 정보 조회 엔드포인트
    public ApiResponse<Page<UserRankResponse>> getUsersByExp(@PageableDefault(size = 50,sort = "exp",direction = Sort.Direction.DESC)
                                                             Pageable pageable){//페이지네이션 설정(페이지:50, exp 내림차순)
        return ApiResponse.onSuccess(userQueryService.getUsersByExp(pageable));
    }
}
