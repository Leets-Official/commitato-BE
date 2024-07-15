package com.leets.commitatobe.domain.login.usecase;

import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginQueryService {
    // AUTH 헤더에서 엑세스 토큰을 이용해 유저 아이디를 불러오는 함수
    GitHubDto getGitHubUser(HttpServletRequest request);

}
