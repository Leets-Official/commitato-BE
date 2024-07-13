package com.leets.commitatobe.domain.login.controller;

import com.leets.commitatobe.domain.login.dto.GitHubDto.UserDto;
import com.leets.commitatobe.domain.login.dto.JwtDto.JwtResponse;
import com.leets.commitatobe.global.config.CustomOAuth2UserService;
import com.leets.commitatobe.domain.login.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    private final CustomOAuth2UserService customOAuth2UserService;

    // 로그인 로직 처리부분
    @GetMapping("/github")
    public void redirectToGitHub(HttpServletResponse response) {
        loginService.redirect(response);
    }

    @GetMapping("/callback")
    public ResponseEntity<JwtResponse> githubCallback(@RequestParam("code") String code, HttpServletResponse response) {
        // GitHub에서 받은 인가 코드로 액세스 토큰 요청
        String accessToken = loginService.gitHubLogin(code);
        // 액세스 토큰을 이용하여 JWT 생성
        JwtResponse jwt = customOAuth2UserService.generateJwt(accessToken);

        // 헤더 설정
        response.setHeader("Authentication", "Bearer " + jwt.accessToken());

        return ResponseEntity.ok(jwt);
    }

    // 테스트용 API
    @GetMapping("/test")
    public ResponseEntity<UserDto> test(HttpServletRequest request) {
        UserDto user = loginService.getUserId(request);

        return ResponseEntity.ok(user);
    }

}