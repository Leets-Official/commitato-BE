package com.leets.commitatobe.domain.commit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "commit")
@Getter
@NoArgsConstructor
public class Commit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "commit_id")
    private UUID id;

    @Column
    private Integer cnt;

    @Column
    private LocalDateTime commitDate;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    @Column(name = "is_calculated")
    private boolean isCalculated;//경험치 계산 여부를 나타낸다.

    public static Commit create(LocalDateTime commitDate, Integer cnt, User user) {
        return Commit.builder()
                .commitDate(commitDate)
                .cnt(cnt)
                .user(user)
                .build();
    }

    @Builder
    public Commit(LocalDateTime commitDate, Integer cnt, User user) {
        this.commitDate = commitDate;
        this.cnt = cnt;
        this.user = user;
    }

    public void updateCnt(Integer cnt) {
        this.cnt = this.cnt + cnt;
    }
    public void setCalculated(boolean calculated){isCalculated=calculated;}//isCalculated 필드 설정
}
