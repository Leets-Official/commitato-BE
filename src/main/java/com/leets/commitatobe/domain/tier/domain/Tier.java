package com.leets.commitatobe.domain.tier.domain;

import jakarta.persistence.*;
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

    public boolean isValid(Integer exp) {
        return this.getRequiredExp() != null && this.getRequiredExp() <= exp;
    }
}
