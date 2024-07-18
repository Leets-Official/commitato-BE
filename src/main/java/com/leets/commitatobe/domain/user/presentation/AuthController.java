package com.leets.commitatobe.domain.user.presentation;

import com.leets.commitatobe.global.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

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
}
