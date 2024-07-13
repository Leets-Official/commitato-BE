package com.leets.commitatobe.global.config;

import com.leets.commitatobe.domain.login.Member;
import com.leets.commitatobe.domain.login.dto.JwtDto.JwtResponse;
import com.leets.commitatobe.domain.login.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

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

    // 깃허브에서 가져온 사용자 정보 받아오기
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2User를 반환
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuth2User.getAttributes(),
            "login");

    }

    @Transactional
    public JwtResponse loadUserAndJwt(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadUser(userRequest);

        // 깃허브에서 가져온 사용자 정보를 이용해 jwt 생성
        String githubId = oAuth2User.getAttribute("login");
        String accessToken = userRequest.getAccessToken().getTokenValue();

        // GitHub에서 받은 사용자 정보를 바탕으로 Member 엔티티를 조회하거나 새로 생성
        Member member = memberRepository.findByGithubId(githubId)
            .orElseGet(() -> createNewMember(githubId, accessToken));

        // 4. 인증 정보를 기반으로 jwt 생성
        JwtResponse jwt = jwtProvider.generateTokenDto(member.getGithubId());

        // OAuth2User와 JwtDto를 포함하는 DTO 반환
        return jwt;
    }

    @Transactional
    // Member 생성 메서드
    public Member createNewMember(String githubId, String accessToken) {
        Member member = Member.builder()
            .githubId(githubId)
            .githubAccessToken(accessToken)
            .roles(Collections.singletonList("ROLE_USER"))
            .build();

        Member savedMember = memberRepository.save(member);

        return savedMember;
    }
}
