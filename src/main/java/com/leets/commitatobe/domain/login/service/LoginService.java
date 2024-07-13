package com.leets.commitatobe.domain.login.service;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_JSON_PARSING_ERROR;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_TOKEN_GENERATION_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.commitatobe.domain.login.dto.GitHubDto.UserDto;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    private final JwtProvider jwtProvider;

    // 깃허브에서 accessToken가져오는 로직
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

    public UserDto getUserId(HttpServletRequest request) {
        // 헤더에서 액세스 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
        }

        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("JWT 토큰이 존재하지 않습니다.");
        }

        // 액세스 토큰을 이용하여 사용자 정보 가져오기
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        return UserDto.builder()
            .userId(authentication.getName())
            .build();
    }

    public void redirect(HttpServletResponse response){
        String url = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=user";
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            log.error("GitHub redirect failed", e);
        }
    }
}


