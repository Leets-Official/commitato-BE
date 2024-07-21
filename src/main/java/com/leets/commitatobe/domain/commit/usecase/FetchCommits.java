package com.leets.commitatobe.domain.commit.usecase;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.commit.domain.repository.CommitRepository;
import com.leets.commitatobe.domain.commit.presentation.dto.response.CommitResponse;
import com.leets.commitatobe.domain.login.usecase.LoginCommandServiceImpl;
import com.leets.commitatobe.domain.login.usecase.LoginQueryService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.domain.user.usecase.UserQueryService;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
import com.leets.commitatobe.global.response.code.status.SuccessStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class FetchCommits {
    private final CommitRepository commitRepository;
    private final UserRepository userRepository;
    private final GitHubService gitHubService; // GitHub API 통신
    private final LoginCommandServiceImpl loginCommandService;
    private final LoginQueryService loginQueryService;
    private final UserQueryService userQueryService;

    public CommitResponse execute(HttpServletRequest request) {
        String gitHubId = loginQueryService.getGitHubId(request);
        User user = userRepository.findByGithubId(gitHubId)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 깃허브 닉네임과 일치하는 유저를 찾을 수 없음: " + gitHubId));

        LocalDateTime dateTime;
        try {
            dateTime = commitRepository.findAllByUser(user).stream()
                    .max(Comparator.comparing(Commit::getUpdatedAt))
                    .orElseThrow(() -> new ApiException(ErrorStatus._COMMIT_NOT_FOUND))
                    .getUpdatedAt();
        } catch (ApiException e) {
            dateTime = user.getCreatedAt().toLocalDate().atStartOfDay();
        }

        try {
            // 기존: Github API Access Token 저장
//            gitHubService.updateToken(loginCommandService.gitHubLogin(gitHubId));

            //변경: DB에서 엑세스 토큰 불러오도록 방식 변경
            String gitHubaccessToken = userQueryService.getUserGitHubAccessToken(gitHubId);
            gitHubService.updateToken(gitHubaccessToken);

            List<String> repos = gitHubService.fetchRepos(gitHubId);
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            LocalDateTime finalDateTime = dateTime; // 오류 방지

            for (String fullName : repos) {
                CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                    try {
                        gitHubService.countCommits(fullName, gitHubId, finalDateTime);
                    } catch (IOException e) {
                        throw new ApiException(ErrorStatus._GIT_URL_INCORRECT);
                    }
                }, executor);
                futures.add(voidCompletableFuture);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.join();

            executor.shutdown();

            saveCommits(user);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new CommitResponse(SuccessStatus._OK.getMessage());
    }

    private void saveCommits(User user) {
        // 날짜별 커밋 수 DB에 저장
        for (Map.Entry<LocalDateTime, Integer> entry : gitHubService.getCommitsByDate().entrySet()) {
            Commit commit = commitRepository.findByCommitDateAndUser(entry.getKey(), user)
                    .orElse(Commit.create(entry.getKey(), 0, user));
            commit.updateCnt(entry.getValue());
            commitRepository.save(commit);
        }
    }
}
