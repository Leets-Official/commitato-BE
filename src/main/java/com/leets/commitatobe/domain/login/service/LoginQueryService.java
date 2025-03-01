package com.leets.commitatobe.domain.login.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.domain.login.dto.GitHubDto;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginQueryService {

	public GitHubDto getGitHubUser() {
		return getUserDetails().getGitHubDto();
	}

	public String getGitHubId() {
		return getUserDetails().getGithubId();
	}

	private CustomUserDetails getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
			throw new ApiException(ErrorStatus._JWT_NOT_FOUND);
		}
		return userDetails;
	}
}
