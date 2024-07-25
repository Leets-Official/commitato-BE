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
public class Tier extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tier_id")
    private UUID id;

    private String tierName;
    private String characterUrl;
    private String badgeUrl;

    @Column(nullable = false)
    private Integer requiredExp;

    @OneToMany(mappedBy = "tier")
    private List<User> userList;

    public Tier(UUID id,String tierName, String characterUrl, String badgeUrl, Integer requiredExp, List<User>userList){
        this.id=id;
        this.tierName=tierName;
        this.characterUrl=characterUrl;
        this.badgeUrl=badgeUrl;
        this.requiredExp=requiredExp;
        this.userList=userList;
    }

}
