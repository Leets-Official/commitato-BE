package com.leets.commitatobe.domain.tier.presentation;

import com.leets.commitatobe.domain.tier.presentation.dto.response.TierResponse;
import com.leets.commitatobe.domain.tier.usecase.CreateInitialTiers;
import com.leets.commitatobe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Tier 컨트롤러", description = "테스트를 위해 초기 티어를 자동적으로 생성합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tier")
public class TierController {
    private final CreateInitialTiers createInitialTiers;


    @Operation(
            summary = "초기 티어 생성",
            description = "테스트를 위해 서비스에서 사용될 초기 티어를 만듭니다.")
    @PostMapping("/initial")
    public ApiResponse<TierResponse> createInitialTiers() {
        return ApiResponse.onSuccess(createInitialTiers.execute());
    }
}
