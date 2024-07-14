package com.leets.commitatobe.domain.login.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.leets.commitatobe.domain.login.presentation.dto.GitHubDto;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String githubId;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String githubId, boolean isAccountNonExpired,
        boolean isAccountNonLocked, boolean isCredentialsNonExpired,
        boolean isEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.githubId = githubId;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public String getGithubId() {
        return githubId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public GitHubDto getGitHubDto() {
        return new GitHubDto(this.githubId);
    }
}

