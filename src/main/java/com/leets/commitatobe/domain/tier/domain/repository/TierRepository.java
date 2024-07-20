package com.leets.commitatobe.domain.tier.domain.repository;

import com.leets.commitatobe.domain.tier.domain.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TierRepository extends JpaRepository<Tier, UUID> {
    Optional<Tier> findByTierName(String tierName);
}
