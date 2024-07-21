package com.leets.commitatobe.global.config;

import com.leets.commitatobe.domain.login.presentation.dto.JwtResponse;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.utils.JwtProvider;
import java.time.Instant;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // Jwt 생성 메인 함수
    public JwtResponse generateJwt (String authCode){
        // 사용자 정보를 가져와 jwt 생성
        // ClientRegistration 객체 생성
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

        // OAuth2UserRequest 생성
        OAuth2UserRequest userRequest = new OAuth2UserRequest(
            clientRegistration,
            new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, authCode, Instant.now(), Instant.now().plusSeconds(3600))
        );

        // 사용자 정보와 JWT 토큰 생성
        JwtResponse jwt = loadUserAndJwt(userRequest);

        return jwt;
    }

    // 깃허브에서 사용자 정보 받아오기
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2User를 반환
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuth2User.getAttributes(),
            "login");

    }

    // 사용자 정보를 바탕으로 jwt 생성
    public JwtResponse loadUserAndJwt(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadUser(userRequest);
        String githubId = oAuth2User.getAttribute("login");

        JwtResponse jwt = jwtProvider.generateTokenDto(githubId);

        // GitHub에서 받은 사용자 정보를 바탕으로 Member 엔티티를 조회하거나 새로 생성
        userRepository.findByGithubId(githubId)
            .orElseGet(() -> createNewUser(oAuth2User, jwt.refreshToken()));

        return jwt;
    }

    // User 생성 메서드
    public User createNewUser(OAuth2User oAuth2User, String refreshToken) {
        String githubId = oAuth2User.getAttribute("login");
        String username = oAuth2User.getAttribute("name");
        String profileImage = oAuth2User.getAttribute("avatar_url");

        User user = User.builder()
            .githubId(githubId)
            .username(username)
            .profileImage(profileImage)
            .build();

        User savedUser = userRepository.save(user);

        return savedUser;
    }
}