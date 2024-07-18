package com.leets.commitatobe.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(nullable = true)
    private String username;

    @Column(nullable = false)
    private String githubId;

    @Column
    private String refreshToken;

    @Column
    private String profileImage;

    @Column
    private Integer exp;

    @Column
    private Integer commitDays;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Commit> commitList;

    @ManyToOne
    @JoinColumn(name="tier_id")
    private Tier tier;
}
