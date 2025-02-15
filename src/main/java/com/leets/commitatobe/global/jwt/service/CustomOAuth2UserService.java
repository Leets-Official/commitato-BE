package com.leets.commitatobe.global.jwt.service;

import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.commitatobe.domain.login.usecase.LoginCommandService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.jwt.dto.JwtResponse;
import com.leets.commitatobe.global.jwt.provider.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	@Value("${spring.security.oauth2.client.registration.github.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.github.client-secret}")
	private String clientSecret;

	@Autowired
	private JwtProvider jwtProvider;

	private final UserRepository userRepository;

	@Autowired
	private LoginCommandService loginCommandService;

	public JwtResponse generateJwt(String gitHubAccessToken) {
		ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
			.clientId(clientId)
			.clientSecret(clientSecret)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("{baseUrl}/api/auth/github/callback")
			.tokenUri("https://github.com/login/oauth/access_token")
			.authorizationUri("https://github.com/login/oauth/authorize")
			.userInfoUri("https://api.github.com/user")
			.userNameAttributeName("login")  // GitHub의 로그인 이름 속성을 지정
			.build();

		OAuth2UserRequest userRequest = new OAuth2UserRequest(
			clientRegistration,
			new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, gitHubAccessToken, Instant.now(),
				Instant.now().plusSeconds(3600))
		);

		JwtResponse jwt = loadUserAndJwt(userRequest, gitHubAccessToken);

		return jwt;
	}

	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
			oAuth2User.getAttributes(),
			"login");

	}

	public JwtResponse loadUserAndJwt(OAuth2UserRequest userRequest, String gitHubAccessToken) throws
		OAuth2AuthenticationException {
		OAuth2User oAuth2User = loadUser(userRequest);
		String githubId = oAuth2User.getAttribute("login");

		JwtResponse jwt = jwtProvider.generateTokenDto(githubId);

		userRepository.findByGithubId(githubId)
			.orElseGet(() -> createNewUser(oAuth2User, gitHubAccessToken));

		return jwt;
	}

	public User createNewUser(OAuth2User oAuth2User, String gitHubAccessToken) {
		String githubId = oAuth2User.getAttribute("login");
		String username = oAuth2User.getAttribute("name");
		String profileImage = oAuth2User.getAttribute("avatar_url");

		String encryptedGitHubAccessToken = loginCommandService.encrypt(gitHubAccessToken);

		User user = User.builder()
			.githubId(githubId)
			.username(username)
			.profileImage(profileImage)
			.exp(0)
			.gitHubAccessToken(encryptedGitHubAccessToken)
			.build();

		return userRepository.save(user);
	}
}
