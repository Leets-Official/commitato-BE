package com.leets.commitatobe.domain.user.usecase;

import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserRankResponse;
import com.leets.commitatobe.domain.user.presentation.dto.response.UserResponse;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
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
    @Override
    public List<UserResponse> searchUsersByUsername(String username) throws ApiException {// 유저 이름으로 유저 정보 검색
        List<User> users=userRepository.findByUsernameContainingIgnoreCase(username);// 유저 이름으로 데이터베이스에서 검색

        if(users.isEmpty()){//검색 결과가 없다면
            throw new ApiException(ErrorStatus._USER_NOT_FOUND);//예외처리
        }

        return users.stream()
                .map(user->new UserResponse(
                        user.getUsername(),
                        user.getExp(),
                        user.getTier().getTierName()
                ))
                .collect(Collectors.toList());//검색 결과를 UserResponse 객체로 변환하여 리스트 변환
    }
    @Override
    public Page<UserRankResponse> getUsersByExp(Pageable pageable){//경험치 순으로 페이징된 유저 정보 조회
        return userRepository.findAllByOrderByExpDesc(pageable)//UserRankResponse 객체로 바꿔 페이지 객체에 넣어 보관
                .map(user -> new UserRankResponse(
                        user.getUsername(),
                        user.getExp()
                ));
    }
}