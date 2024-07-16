package com.leets.commitatobe.domain.commit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
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
    private Date commitDate;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    public static Commit create(Date commitDate, Integer cnt, User user) {
        return Commit.builder()
                .commitDate(commitDate)
                .cnt(cnt)
                .user(user)
                .build();
    }

    @Builder
    public Commit(Date commitDate, Integer cnt, User user) {
        this.commitDate = commitDate;
        this.cnt = cnt;
        this.user = user;
    }

    public void updateCnt(Integer cnt) {
        this.cnt = this.cnt + cnt;
    }
}
