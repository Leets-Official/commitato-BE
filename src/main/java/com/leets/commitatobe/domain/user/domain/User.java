package com.leets.commitatobe.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column
    private String gitHubAccessToken;

    @Column(nullable = true)
    private String username;

    @Column(nullable = false)
    private String githubId;

    @Column
    private String profileImage;

    @Column(nullable = false)
    @Builder.Default
    private Integer exp = 0;

    @Column
    private Integer commitDays;

    @Column
    private Integer consecutiveCommitDays;

    @Column
    private Integer totalCommitCount;

    @Column
    private Integer todayCommitCount;

    @Column
    private Integer ranking;// 랭킹 추가

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Commit> commitList;

    @ManyToOne
    @JoinColumn(name="tier_id")
    private Tier tier;

    public void updateExp(Integer exp){
        this.exp=exp;
    }

    public void updateTier(Tier tier){
        this.tier=tier;
    }

    public void updateConsecutiveCommitDays(Integer consecutiveCommitDays){
        this.consecutiveCommitDays=consecutiveCommitDays;
    }

    public void updateTotalCommitCount(Integer totalCommitCount){
        this.totalCommitCount=totalCommitCount;
    }

    public void updateTodayCommitCount(Integer todayCommitCount){
        this.todayCommitCount=todayCommitCount;
    }

    public void updateRank(Integer ranking) { this.ranking = ranking; }
}
