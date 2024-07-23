package com.leets.commitatobe.domain.user.usecase;

import static com.leets.commitatobe.global.response.code.status.ErrorStatus._USER_NOT_FOUND;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.domain.login.usecase.LoginCommandService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final LoginCommandService loginCommandService;

    @Override
    public String getUserGitHubAccessToken(String githubId) {
        Optional<User> user = userRepository.findByGithubId(githubId);

        if(user == null){
            throw new ApiException(_USER_NOT_FOUND);
        }

        String gitHubAccessToken = user.get().getGitHubAccessToken();

        String decodedGitHubAccessToken = loginCommandService.decrypt(gitHubAccessToken);

        return decodedGitHubAccessToken;
    }
}