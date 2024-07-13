package com.leets.commitatobe.domain.login.controller;

import com.leets.commitatobe.domain.login.Member;
import com.leets.commitatobe.domain.login.dto.GitHubDto.UserDto;
import com.leets.commitatobe.domain.login.dto.JwtDto.JwtResponse;
import com.leets.commitatobe.global.config.CustomOAuth2UserService;
import com.leets.commitatobe.domain.login.service.LoginService;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class LoginController {

    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/github")
    public ResponseEntity<JwtResponse> login(HttpServletResponse response, @RequestParam("code") String code) {
        String accessToken = loginService.gitHubLogin(code);

        JwtResponse jwt = customOAuth2UserService.generateJwt(accessToken);

        log.info("{}", jwt.accessToken());

        // 쿠키 설정
        loginService.addAuthCookies(response, jwt);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/test")
    public ResponseEntity<UserDto> test(HttpServletRequest request) {
        // 쿠키에서 액세스 토큰 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        log.info("Access Token from cookie: {}", accessToken);

        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("JWT 토큰이 존재하지 않습니다.");
        }

        // 액세스 토큰을 이용하여 사용자 정보 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        UserDto user = UserDto.builder()
            .userId(authentication.getName())
            .build();


        return ResponseEntity.ok(user);
    }

}