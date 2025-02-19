package com.leets.commitatobe.domain.tier.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
