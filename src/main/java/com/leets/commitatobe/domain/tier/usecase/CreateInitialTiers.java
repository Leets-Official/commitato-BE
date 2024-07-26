package com.leets.commitatobe.domain.tier.usecase;

import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.domain.tier.domain.repository.TierRepository;
import com.leets.commitatobe.domain.tier.presentation.dto.response.TierResponse;
import com.leets.commitatobe.global.response.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateInitialTiers {
    private final TierRepository tierRepository;

    public TierResponse execute() {
        List<Tier> initialTiers = new ArrayList<>();

        initialTiers.add(Tier.createTier("바보 감자", "https://d1ds22ndweg1nu.cloudfront.net/potato/stupidPotato.png", 0, new ArrayList<>()));
        initialTiers.add(Tier.createTier("말하는 감자", "https://d1ds22ndweg1nu.cloudfront.net/potato/speakingPotato.png", 1000, new ArrayList<>()));
        initialTiers.add(Tier.createTier("개발자 감자", "https://d1ds22ndweg1nu.cloudfront.net/potato/developerPotato.png", 15000, new ArrayList<>()));
        initialTiers.add(Tier.createTier("CEO 감자", "https://d1ds22ndweg1nu.cloudfront.net/potato/CEOPotato.png", 33000, new ArrayList<>()));

        tierRepository.saveAll(initialTiers);

        return new TierResponse(SuccessStatus._OK.getMessage());
    }
}
