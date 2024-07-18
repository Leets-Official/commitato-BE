package com.leets.commitatobe.domain.login.presentation;

import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;
import com.leets.commitatobe.domain.login.presentation.dto.JwtResponse;
import com.leets.commitatobe.domain.login.usecase.LoginCommandService;
import com.leets.commitatobe.domain.login.usecase.LoginQueryService;
import com.leets.commitatobe.global.config.CustomOAuth2UserService;
import com.leets.commitatobe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth2 컨트롤러", description = "OAuth2 로그인 및 콜백을 처리합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {

    private final LoginCommandService loginCommandService;
    private final LoginQueryService loginQueryService;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Operation(
        summary = "GitHub 로그인 리다이렉트",
        description = "GitHub OAuth2 로그인 페이지로 리다이렉트합니다."
    )
    @GetMapping("/github")
    public void redirectToGitHub(HttpServletResponse response) {
        loginCommandService.redirect(response);
    }

    @Operation(
        summary = "GitHub OAuth2 콜백",
        description = "GitHub OAuth2 로그인 후 콜백을 처리하고, 인가 코드를 액세스 토큰으로 교환한 후 JWT를 생성합니다."
    )
    @Parameter(
        name = "code",
        description = "GitHub 로그인 후 받은 인가 코드",
        required = true,
        example = "123456"
    )
    @GetMapping("/callback")
    public ApiResponse<JwtResponse> githubCallback(@RequestParam("code") String code, HttpServletResponse response) {
        // GitHub에서 받은 인가 코드로 액세스 토큰 요청
        String accessToken = loginCommandService.gitHubLogin(code);
        // 액세스 토큰을 이용하여 JWT 생성
        JwtResponse jwt = customOAuth2UserService.generateJwt(accessToken);

        // 액세스 토큰을 헤더에 설정
        response.setHeader("Authentication", "Bearer " + jwt.accessToken());

        // 리프레시 토큰을 httpOnly 쿠키에 설정
        loginCommandService.setRefreshTokenCookie(response, jwt.refreshToken());


        return ApiResponse.onSuccess(jwt);
    }


    // 테스트용 APIzz
    @GetMapping("/test")
    public ApiResponse<GitHubDto> test(HttpServletRequest request) {
        GitHubDto user = loginQueryService.getGitHubUser(request);
        return ApiResponse.onSuccess(user);
    }

}