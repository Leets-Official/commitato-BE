package com.leets.commitatobe.domain.login.usecase;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginQueryServiceImpl implements LoginQueryService {

	@Override
	public GitHubDto getGitHubUser(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
			throw new ApiException(ErrorStatus._JWT_NOT_FOUND);
		}

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		return userDetails.getGitHubDto();
	}

	@Override
	public String getGitHubId(HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
			throw new ApiException(ErrorStatus._JWT_NOT_FOUND);
		}

		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();

		return userDetails.getGithubId();
	}
}
