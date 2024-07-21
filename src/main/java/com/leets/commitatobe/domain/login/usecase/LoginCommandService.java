package com.leets.commitatobe.domain.login.usecase;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginCommandService {
    // 깃허브 accessToken가져오는 함수
    String gitHubLogin(String authCode);

    // 로그인시 리다이렉트 처리 함수
    void redirect(HttpServletResponse response);

    // 응답에 쿠키 설정 함수
    void setRefreshTokenCookie(HttpServletResponse response, String refreshToken);

    String encrypt(String token);

    String decrypt(String token);

}
