package com.leets.commitatobe.domain.login.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.leets.commitatobe.domain.login.dto.GitHubDto;

public class CustomUserDetails implements UserDetails {

	private final String username;
	private final String githubId;
	private final boolean isAccountNonExpired;
	private final boolean isAccountNonLocked;
	private final boolean isCredentialsNonExpired;
	private final boolean isEnabled;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(String username, String githubId, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.githubId = githubId;
		this.isAccountNonExpired = true;
		this.isAccountNonLocked = true;
		this.isCredentialsNonExpired = true;
		this.isEnabled = true;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getGithubId() {
		return githubId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public GitHubDto getGitHubDto() {
		return new GitHubDto(this.githubId);
	}
}
