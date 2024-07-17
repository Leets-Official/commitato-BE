package com.leets.commitatobe.domain.commit.presentation;
import com.leets.commitatobe.domain.commit.presentation.dto.response.CommitResponse;
import com.leets.commitatobe.domain.commit.usecase.FetchCommits;
import com.leets.commitatobe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/commit")
public class CommitController {
    private final FetchCommits fetchCommits;

    @Operation(
            summary = "커밋 기록 불러오기",
            description = "GitHub에서 커밋 기록을 가져와 DB에 저장합니다.")
    @PostMapping("/fetch")
    public ApiResponse<CommitResponse> fetchCommits(HttpServletRequest request) {
        return ApiResponse.onSuccess(fetchCommits.execute(request));
    }
}
