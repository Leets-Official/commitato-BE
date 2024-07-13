package com.leets.commitatobe.domain.login.service;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_JSON_PARSING_ERROR;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._GITHUB_TOKEN_GENERATION_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.commitatobe.global.exception.ApiException;
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
public class LoginService {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private final CustomOAuth2UserService customOAuth2UserService;

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
}


