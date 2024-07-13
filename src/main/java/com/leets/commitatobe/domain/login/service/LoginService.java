package com.leets.commitatobe.domain.login.service;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_JSON_PARSING_ERROR;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_TOKEN_GENERATION_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.commitatobe.domain.login.dto.GitHubDto.UserDto;
import com.leets.commitatobe.domain.login.dto.JwtDto.JwtResponse;
import com.leets.commitatobe.global.config.CustomOAuth2UserService;
import com.leets.commitatobe.global.exception.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
public class LoginService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Autowired
    public LoginService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    // 깃허브에서 accessToken가져오는 로직
    public String gitHubLogin(String authCode) {
        RestTemplate restTemplate = new RestTemplate();

        // GitHub 액세스 토큰 요청
        String tokenRequestUrl = "https://github.com/login/oauth/access_token" +
            "?client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&code=" + authCode +
            "&redirect_uri=" + "http://localhost:3000/api/auth/github/callback";

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

    // 쿠키 추가
    public void addAuthCookies(HttpServletResponse response, JwtResponse jwt) {
        // 엑세스 토큰 쿠키
        Cookie accessTokenCookie = new Cookie("accessToken", jwt.accessToken());
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60*60); // 한시간
        response.addCookie(accessTokenCookie);

        // 리프레쉬토큰 쿠키
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwt.refreshToken());
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 일주일
        response.addCookie(refreshTokenCookie);
    }

    // 엑세스 토큰을 이용해 유저 정보 받아오는 로직
    public UserDto getUserInfo(String accessToken) {
        String url = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDto.class);

        return response.getBody();
    }
}


