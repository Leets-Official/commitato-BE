package com.leets.commitatobe.domain.login.service;

import static ch.qos.logback.core.encoder.ByteArrayUtil.*;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leets.commitatobe.global.exception.ApiException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginCommandService {

	// 암호화 알고리즘
	private final String ENCODING_ALGORITHM = "AES/CBC/PKCS5Padding";

	@Value("${spring.security.oauth2.client.registration.github.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.github.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
	private String redirectUri;

	// 깃허브 엑세스 토큰 암호화를 위한 변수
	@Value("${jwt.aes-secret}")
	private String aesSecret;

	@Value("${jwt.iv-secret}")
	private String ivSecret;

	public String gitHubLogin(String authCode) {
		WebClient webClient = WebClient.builder()
			.baseUrl("https://github.com")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.build();

		// GitHub 액세스 토큰 요청
		String tokenRequestUrl = "/login/oauth/access_token" +
			"?client_id=" + clientId +
			"&client_secret=" + clientSecret +
			"&code=" + authCode +
			"&redirect_uri=" + redirectUri;

		Mono<String> responseMono = webClient.post()
			.uri(tokenRequestUrl)
			.retrieve()
			.bodyToMono(String.class);

		String responseBody = responseMono.block();

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

	public void redirect(HttpServletResponse response) {
		// TODO: user로 scope를 설정하면 읽고 쓰는 권한 모두를 가져오게 됨, 이후 개발하며 개발 범위에 맞춰 수정이 필요함
		String url = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri;
		try {
			response.sendRedirect(url);
		} catch (Exception e) {
			throw new ApiException(_REDIRECT_ERROR);
		}
	}

	public String encrypt(String token) {
		byte[] encrypted;
		try {
			Cipher cipher = Cipher.getInstance(ENCODING_ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(hexStringToByteArray(aesSecret), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(hexStringToByteArray(ivSecret));
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

			encrypted = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new ApiException(_ENCRYPT_ERROR);
		}
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String decrypt(String token) {
		byte[] decrypted;

		try {
			Cipher cipher = Cipher.getInstance(ENCODING_ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(hexStringToByteArray(aesSecret), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(hexStringToByteArray(ivSecret));
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

			byte[] decodedBytes = Base64.getDecoder().decode(token);
			decrypted = cipher.doFinal(decodedBytes);
		} catch (Exception e) {
			throw new ApiException(_DECRYPT_ERROR);
		}
		return new String(decrypted, StandardCharsets.UTF_8);
	}
}
