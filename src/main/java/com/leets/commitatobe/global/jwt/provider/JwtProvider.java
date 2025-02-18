package com.leets.commitatobe.global.jwt.provider;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.global.jwt.dto.JwtResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
	private final Key key;
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 360; //access 6시간

	public JwtProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// AccessToken, RefreshToken을 생성하는 메서드
	public JwtResponse generateTokenDto(String githubId) {
		String accessToken = generateAccessToken(githubId);
		return new JwtResponse("bearer", accessToken);
	}

	// Access Token 생성
	public String generateAccessToken(String githubId) {
		long now = (new Date()).getTime();
		Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
		return Jwts.builder()
			.setSubject(githubId) // "sub" 클레임에 사용자 ID 저장
			.claim("auth", "ROLE_USER") // 권한 정보 추가
			.setExpiration(accessTokenExpiresIn)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	// Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String accessToken) {

		// jwt 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		// 클레임에서 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		// CustomUserDetails 객체를 만들어서 Authentication return
		CustomUserDetails principal = CustomUserDetails.of(
			claims.getSubject(), claims.getSubject(), authorities
		);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	// 토큰 정보 검증 매서드
	public boolean validateToken(String token) {

		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}

	// access 토큰 검증과 파싱 수행
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(accessToken)
				.getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public String getGithubIdFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
		String githubId = claims.getSubject();
		return githubId;
	}
}
