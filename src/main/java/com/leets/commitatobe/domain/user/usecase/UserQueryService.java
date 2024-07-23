package com.leets.commitatobe.domain.user.usecase;


import jakarta.servlet.http.HttpServletRequest;

public interface UserQueryService {
    String getUserGitHubAccessToken(String githubId);
}