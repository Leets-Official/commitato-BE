package com.leets.commitatobe.domain.commit.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import reactor.core.publisher.Mono;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class GitHubService {
	private final String GITHUB_API_URL = "https://api.github.com";
	private String AUTH_TOKEN;
	private final Map<LocalDateTime, Integer> commitsByDate = new HashMap<>();
	private final WebClient webClient = WebClient.builder()
		.baseUrl(GITHUB_API_URL)
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
		.exchangeStrategies(ExchangeStrategies.builder()
			.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1MB
			.build())
		.build();

	@Value("${server-uri}")
	private String SERVER_URI;

	// GitHub repository 이름 저장
	public List<String> fetchRepos(String gitHubUsername) {
		JsonArray repos = getConnection("/user/repos?type=all&sort=pushed&per_page=100");

		if (repos == null) {
			return new ArrayList<>();
		}

		List<String> repoFullNames = new ArrayList<>();
		for (int i = 0; i < repos.size(); i++) {
			String fullName = repos.get(i).getAsJsonObject().get("full_name").getAsString();
			repoFullNames.add(fullName);
		}

		ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		return forkJoinPool.submit(() ->
			repoFullNames.parallelStream()
				.filter(fullName -> isContributor(fullName, gitHubUsername))
				.toList()
		).join();
	}

	// 자신이 해당 repository의 기여자 인지 확인
	private boolean isContributor(String fullName, String gitHubUsername) {
		if (fullName.contains(gitHubUsername)) {
			return true;
		}

		JsonArray contributors = getConnection("/repos/" + fullName + "/contributors");

		if (contributors == null) {
			return false;
		}

		for (int i = 0; i < contributors.size(); i++) {
			JsonObject contributor = contributors.get(i).getAsJsonObject();
			String contributorLogin = contributor.get("login").getAsString();

			if (contributorLogin.equals(gitHubUsername)) {
				return true;
			}
		}

		return false;
	}

	// commit을 일별로 정리
	public void countCommits(String fullName, String gitHubUsername, LocalDateTime date) {
		int page = 1;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		while (true) {
			JsonArray commits;

			try{
				commits = getConnection("/repos/" + fullName + "/commits?page=" + page + "&per_page=100");
			} catch (Exception e) {
				return;
			}

			if (commits == null || commits.isEmpty()) {
				return;
			}

			for (int i = 0; i < commits.size(); i++) {
				String commitDateTime = getCommitDateTime(commits.get(i).getAsJsonObject());

				int comparisonResult = commitDateTime.compareTo(formatToISO8601(date));

				if (comparisonResult < 0 || !commits.get(i)
					.getAsJsonObject()
					.get("author")
					.getAsJsonObject()
					.get("login")
					.getAsString()
					.equals(gitHubUsername)) {
					continue;
				}

				LocalDateTime commitDate = LocalDate.parse(commitDateTime.substring(0, 10), formatter).atStartOfDay();

				synchronized (commitsByDate) {
					commitsByDate.put(commitDate, commitsByDate.getOrDefault(commitDate, 0) + 1);
				}
			}

			page++;
		}
	}

	// http 연결
	private JsonArray getConnection(String url) {
		Mono<JsonArray> response = webClient.get()
			.uri(url)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + AUTH_TOKEN)
			.retrieve()
			.onStatus(status -> status == HttpStatus.UNAUTHORIZED, clientResponse ->
				// AUTH_TOKEN이 유효하지 않으면 리다이렉트
				webClient.get()
					.uri(SERVER_URI + "/login/github")
					.retrieve()
					.bodyToMono(Void.class)
					.then(Mono.error(new RuntimeException("Unauthorized")))
			)
			.bodyToMono(String.class)
			.map(res -> JsonParser.parseString(res).getAsJsonArray());

		return response.block();
	}

	// commit 시간 추출
	private String getCommitDateTime(JsonObject commit) {
		String originCommitDateTime = commit.get("commit").getAsJsonObject() // UTC+0
			.get("author").getAsJsonObject()
			.get("date").getAsString();

		// 입력된 시간 문자열을 LocalDateTime으로 변환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		LocalDateTime dateTime = LocalDateTime.parse(originCommitDateTime, formatter);

		// 9시간 추가 -> UTC+9(한국 표준 시)
		return dateTime.plusHours(9).format(formatter);
	}

	// GitHub API에서 제공하는 시간 표현법으로 변환
	private String formatToISO8601(LocalDateTime dateTime) {
		ZonedDateTime zonedDateTime = dateTime.atZone(ZoneOffset.UTC);
		return DateTimeFormatter.ISO_INSTANT.format(zonedDateTime);
	}

	// GitHub Access Token 저장
	public void updateToken(String accessToken) {
		this.AUTH_TOKEN = accessToken;
	}
}
