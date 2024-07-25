package com.leets.commitatobe.domain.tier.domain;

import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Tier extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tier_id")
    private UUID id;

    private String tierName;
    private String characterUrl;

    @Column(nullable = false)
    private Integer requiredExp;

    @OneToMany(mappedBy = "tier")
    private List<User> userList;

    @Builder
    public Tier(String tierName, String characterUrl, Integer requiredExp, List<User>userList){
        this.tierName=tierName;
        this.characterUrl=characterUrl;
        this.requiredExp=requiredExp;
        this.userList=userList;
    }

    public static Tier createTier(String tierName, String characterUrl, Integer requiredExp, List<User> userList){
        return Tier.builder()
                .tierName(tierName)
                .characterUrl(characterUrl)
                .requiredExp(requiredExp)
                .userList(userList)
                .build();
    }
}