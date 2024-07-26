package com.leets.commitatobe.domain.tier.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tier_id")
    private UUID id;

    private String tierName;
    private String characterUrl;

    @Column(nullable = false)
    private Integer requiredExp;

    @Builder
    public Tier(String tierName, String characterUrl, Integer requiredExp) {
        this.tierName = tierName;
        this.characterUrl = characterUrl;
        this.requiredExp = requiredExp;
    }

    public static Tier createTier(String tierName, String characterUrl, Integer requiredExp) {
        return Tier.builder()
                .tierName(tierName)
                .characterUrl(characterUrl)
                .requiredExp(requiredExp)
                .build();
    }

    public boolean isValid(Integer exp) {
        return this.getRequiredExp() != null && this.getRequiredExp() <= exp;
    }
}
