package com.leets.commitatobe.domain.tier.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leets.commitatobe.domain.tier.domain.Tier;

public interface TierRepository extends JpaRepository<Tier, UUID> {
	Optional<Tier> findByTierName(String tierName);
}
