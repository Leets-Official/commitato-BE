package com.leets.commitatobe.domain.login.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leets.commitatobe.domain.login.domain.CustomUserDetails;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByGithubId(username)
			.orElseThrow(() -> new UsernameNotFoundException("해당하는 깃허브 닉네임과 일치하는 유저를 찾을 수 없음: " + username));

		return new CustomUserDetails(
			user.getUsername(),
			user.getGithubId(),
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
