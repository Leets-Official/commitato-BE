package com.leets.commitatobe.domain.tier.domain;

import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tier extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tier_id")
    private UUID id;

    private String tierName;
    private String characterUrl;
    private String badgeUrl;
    private Integer requiredExp;

    @OneToMany(mappedBy = "tier")
    private List<User> userList;

}
