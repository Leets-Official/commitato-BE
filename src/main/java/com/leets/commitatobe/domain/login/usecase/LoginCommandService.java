package com.leets.commitatobe.domain.login.usecase;

import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginCommandService {
    // 깃허브 accessToken가져오는 함수
    String gitHubLogin(String authCode);

    // AUTH 헤더에서 엑세스 토큰을 이용해 유저 아이디를 불러오는 함수
    GitHubDto getGitHubUser(HttpServletRequest request);

    // 로그인시 리다이렉트 처리 함수
    void redirect(HttpServletResponse response);

}
