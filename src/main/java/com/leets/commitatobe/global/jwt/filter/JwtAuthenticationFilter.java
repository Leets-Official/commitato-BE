package com.leets.commitatobe.global.jwt.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.leets.commitatobe.global.jwt.provider.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		// 1. Request Header에서 Jwt 추출
		String token = resolveToken((HttpServletRequest)servletRequest);

		// 2. validateToken으로 토큰 유효성 검사
		if (token != null && jwtProvider.validateToken(token)) {
			// 토큰 유효하면 토큰에서 Authentication 객체를 가지고와 SecurityContext에 저장
			Authentication authentication = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// 다음 필터 또는 서블릿으로 요청과 응답을 전달
		filterChain.doFilter(servletRequest, servletResponse);
	}

	// Request 헤더에서 토큰 정보 추출
	private String resolveToken(HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
