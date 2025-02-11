package com.leets.commitatobe.domain.user.presentation;

import com.leets.commitatobe.domain.login.presentation.dto.JwtResponse;
import com.leets.commitatobe.domain.user.usecase.AuthService;
import com.leets.commitatobe.global.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    //로그아웃
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response){
        //리프레시 토큰 쿠키 삭제
        Cookie myCookie = new Cookie("refreshToken", null);
        myCookie.setMaxAge(0);
        myCookie.setPath("/");
        response.addCookie(myCookie);

        return ApiResponse.onSuccess(null);
    }

    //액세스 토큰 리프레시
    @PostMapping("/refresh")
    public ApiResponse<Object> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        //리프레시 토큰을 통한 액세스 토큰 갱신
        JwtResponse jwt = authService.regenerateAccessToken(request, response);
        // 액세스 토큰을 헤더에 설정
        response.setHeader("Authentication", "Bearer " + jwt.accessToken());

        return ApiResponse.onSuccess(jwt);
    }

}
