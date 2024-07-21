package com.leets.commitatobe.domain.user.usecase;

import com.leets.commitatobe.domain.login.presentation.dto.JwtResponse;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    public JwtResponse regenerateAccessToken(HttpServletRequest request, HttpServletResponse response){
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
            //리프레시 토큰 쿠키 삭제
            Cookie myCookie = new Cookie("refreshToken", null);
            myCookie.setMaxAge(0);
            myCookie.setPath("/");
            response.addCookie(myCookie);
            throw new ApiException(ErrorStatus._REFRESH_TOKEN_EXPIRED);
        }

    }
}
