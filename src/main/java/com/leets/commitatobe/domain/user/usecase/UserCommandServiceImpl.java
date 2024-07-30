package com.leets.commitatobe.domain.user.usecase;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Configuration
public class UserCommandServiceImpl implements UserCommandService {

}
