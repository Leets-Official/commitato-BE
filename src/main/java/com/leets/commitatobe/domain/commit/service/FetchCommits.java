package com.leets.commitatobe.domain.commit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.commit.dto.response.CommitResponse;
import com.leets.commitatobe.domain.commit.repository.CommitRepository;
import com.leets.commitatobe.domain.login.service.LoginQueryService;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.repository.UserRepository;
import com.leets.commitatobe.domain.user.service.UserQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FetchCommits {
	private final CommitRepository commitRepository;
	private final UserRepository userRepository;
	private final GitHubService gitHubService; // GitHub API 통신
	private final LoginQueryService loginQueryService;
	private final ExpService expService;
	private final UserQueryService userQueryService;

	public CommitResponse execute() {
		String gitHubId = loginQueryService.getGitHubId();
		User user = userRepository.findByGithubId(gitHubId)
			.orElseThrow(() -> new UsernameNotFoundException("해당하는 깃허브 닉네임과 일치하는 유저를 찾을 수 없음: " + gitHubId));

		LocalDateTime dateTime = user.getLastCommitUpdateTime();

		if (dateTime == null) {
			dateTime = user.getCreatedAt().toLocalDate().atStartOfDay();
		}

		try {
			// 기존: Github API Access Token 저장
			//            gitHubService.updateToken(loginCommandService.gitHubLogin(gitHubId));

			//변경: DB에서 엑세스 토큰 불러오도록 방식 변경
			gitHubService.updateToken(userQueryService.getUserGitHubAccessToken(gitHubId));

			List<String> repos = gitHubService.fetchRepos(gitHubId);
			ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			List<CompletableFuture<Void>> futures = new ArrayList<>();
			LocalDateTime finalDateTime = dateTime; // 오류 방지

			for (String fullName : repos) {
				CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
					gitHubService.countCommits(fullName, gitHubId, finalDateTime);
				}, executor);
				futures.add(voidCompletableFuture);
			}

			CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
			allFutures.join();

			executor.shutdown();

			user.updateLastCommitUpdateTime(LocalDateTime.now());

			saveCommits(user);

			expService.calculateAndSaveExp(gitHubId);//커밋 가져온 후 경험치 계산 및 저장

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return CommitResponse.of(true, user);
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
