package com.leets.commitatobe.domain.login.controller;

import com.leets.commitatobe.domain.login.dto.JwtDto.JwtResponse;
import com.leets.commitatobe.domain.login.service.CustomOAuth2UserService;
import com.leets.commitatobe.domain.login.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/v1/oauth2")
public class LoginController {

    private final LoginService loginService;

    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/github")
    public ResponseEntity<JwtResponse> login(HttpServletResponse response, @RequestParam("code") String code) {
        String accessToken = loginService.gitHubLogin(code);

        JwtResponse jwt = customOAuth2UserService.generateJwt(accessToken);

        log.info("{}", jwt.accessToken());
        

        // 엑세스 토큰 쿠키
        Cookie accessTokenCookie = new Cookie("accessToken", jwt.accessToken());
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60*60); // 한시간
        response.addCookie(accessTokenCookie);

        // 리프레쉬토큰 쿠카
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwt.refreshToken());
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(jwt);
    }

}