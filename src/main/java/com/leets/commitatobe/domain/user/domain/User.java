package com.leets.commitatobe.domain.user.domain;

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

    // username과 깃허브 아이디가 동일한 값이라서 삭제하겠습니다!
//    @Column(nullable = false)
//    private String username;

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
    private List<Commit> commitList;

    @ManyToOne
    @JoinColumn(name="tier_id")
    private Tier tier;
}
