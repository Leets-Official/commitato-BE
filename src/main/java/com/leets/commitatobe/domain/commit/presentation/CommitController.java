package com.leets.commitatobe.domain.commit.presentation;
import com.leets.commitatobe.domain.commit.usecase.FetchCommits;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> fetchCommits(HttpServletRequest request) {
        return ResponseEntity.ok(fetchCommits.execute(request));
    }
}
