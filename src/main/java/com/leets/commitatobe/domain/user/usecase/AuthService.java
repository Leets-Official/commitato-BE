package com.leets.commitatobe.domain.user.usecase;

import com.leets.commitatobe.domain.login.presentation.dto.JwtResponse;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._REDIRECT_ERROR;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    public JwtResponse regenerateAccessToken(HttpServletRequest request){
        //쿠키에서 리프레시 토큰 찾기
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        try {
            jwtProvider.validateToken(refreshToken);
            String githubId = jwtProvider.getGithubIdFromToken(refreshToken);
            return jwtProvider.regenerateTokenDto(githubId, refreshToken);
        }
        catch (Exception e) {
            throw e;
        }


    }
}
