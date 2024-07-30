package com.leets.commitatobe.domain.user.presentation;

import com.leets.commitatobe.domain.login.usecase.LoginQueryService;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserInfoResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserSearchResponse;
import com.leets.commitatobe.domain.user.usecase.UserQueryService;
import com.leets.commitatobe.global.response.ApiResponse;
import com.leets.commitatobe.global.response.CustomPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public ApiResponse<UserInfoResponse> getUserInfo(@PathVariable String githubId, HttpServletRequest request){
        String myGitHubId = loginQueryService.getGitHubId(request);
        return ApiResponse.onSuccess(userQueryService.findUserInfo(githubId, myGitHubId));
    }

}
