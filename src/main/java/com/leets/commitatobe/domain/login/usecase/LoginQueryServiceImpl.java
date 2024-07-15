package com.leets.commitatobe.domain.login.usecase;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._JWT_NOT_FOUND;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginQueryServiceImpl implements LoginQueryService{

    private final JwtProvider jwtProvider;

    @Override
    public GitHubDto getGitHubUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ApiException(_JWT_NOT_FOUND);
        }

        String accessToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값만 추출
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getGitHubDto();
    }
}
