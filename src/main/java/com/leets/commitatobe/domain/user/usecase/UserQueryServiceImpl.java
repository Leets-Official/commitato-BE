package com.leets.commitatobe.domain.user.usecase;

import com.leets.commitatobe.domain.commit.usecase.ExpService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserResponse;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
import static com.leets.commitatobe.global.response.code.status.ErrorStatus._USER_NOT_FOUND;

import com.leets.commitatobe.domain.login.usecase.LoginCommandService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.utils.JwtProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ExpService expService;
    private final JwtProvider jwtProvider;
    private final LoginCommandService loginCommandService;

    @Override
    @Transactional
    public List<UserResponse> searchUsersByUsername(String username) throws ApiException {// 유저 이름으로 유저 정보 검색
        List<User> users=userRepository.findByUsernameContainingIgnoreCase(username);// 유저 이름으로 데이터베이스에서 검색

        if(users.isEmpty()){//검색 결과가 없다면
            throw new ApiException(ErrorStatus._USER_NOT_FOUND);//예외처리
        }

        return users.stream()
                .map(
                        user->{
                            expService.calculateAndSaveExp(user);
                            return new UserResponse(
                                    user.getUsername(),
                                    user.getExp()!=null?user.getExp():0,
                                    user.getTier() != null ? user.getTier().getTierName() : "StupidPotato"
                            );
                        }
                )
                .collect(Collectors.toList());//검색 결과를 UserResponse 객체로 변환하여 리스트 변환
    }

    @Override
    public Page<UserRankResponse> getUsersByExp(Pageable pageable){//경험치 순으로 페이징된 유저 정보 조회
        Page<User> userPage= userRepository.findAllByOrderByExpDesc(pageable);

        return userPage.map(user->{// 각 사용자의 경험치 최신화 및 UserRankResponse 변환
            expService.calculateAndSaveExp(user);// 경험치 계산 및 저장
            return new UserRankResponse(
                    user.getUsername(),
                    user.getExp());
        });
    }

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