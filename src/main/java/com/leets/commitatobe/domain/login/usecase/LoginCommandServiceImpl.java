package com.leets.commitatobe.domain.login.usecase;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_JSON_PARSING_ERROR;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_TOKEN_GENERATION_ERROR;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._REDIRECT_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCommandServiceImpl implements LoginCommandService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    // 예찬님! 이 함수 사용해서 accessToken 가져오심 됩니다~!
    public String gitHubLogin(String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        // GitHub 액세스 토큰 요청
        String tokenRequestUrl = "https://github.com/login/oauth/access_token" +
            "?client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&code=" + authCode +
            "&redirect_uri=" + redirectUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                tokenRequestUrl,
                HttpMethod.POST,
                entity,
                String.class);
        } catch (Exception e) {
            throw new ApiException(_GITHUB_TOKEN_GENERATION_ERROR);
        }

        String responseBody = response.getBody();

        // JSON 형식의 응답 파싱
        String accessToken;

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> resultMap = mapper.readValue(responseBody, Map.class);
            accessToken = resultMap.get("access_token");
        } catch (Exception e) {
            throw new ApiException(_GITHUB_JSON_PARSING_ERROR);
        }

        if (accessToken == null) {
            throw new ApiException(_GITHUB_TOKEN_GENERATION_ERROR);
        }

        return accessToken;
    }


    public void redirect(HttpServletResponse response){
        // TODO: user로 scope를 설정하면 읽고 쓰는 권한 모두를 가져오게 됨, 이후 개발하며 개발 범위에 맞춰 수정이 필요함
        String url = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=user";
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            throw new ApiException(_REDIRECT_ERROR);
        }
    }

    @Override
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS를 사용할 경우
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
        response.addCookie(refreshTokenCookie);
    }

}


