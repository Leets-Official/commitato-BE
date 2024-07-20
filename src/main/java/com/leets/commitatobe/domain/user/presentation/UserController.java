package com.leets.commitatobe.domain.user.presentation;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserResponse;
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

import java.util.List;


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
    public ApiResponse<List<UserResponse>> searchUsers(@RequestParam("githubId") String githubId){//유저 이름으로 검색하는 앤드포인트
        List<UserResponse> users=userQueryService.searchUsersByGithubId(githubId);//유저 검색 서비스 호출
        return ApiResponse.onSuccess(users);//검색 결과 반환
    }
    @GetMapping("/exp")//경험치 순으로 유저 정보 조회 엔드포인트
    public ApiResponse<Page<UserRankResponse>> getUsersByExp(@PageableDefault(size = 50,sort = "exp",direction = Sort.Direction.DESC)
                                                             Pageable pageable){//페이지네이션 설정(페이지:50, exp 내림차순)
        Page<UserRankResponse> users=userQueryService.getUsersByExp(pageable);
        return ApiResponse.onSuccess(users);
    }
}
