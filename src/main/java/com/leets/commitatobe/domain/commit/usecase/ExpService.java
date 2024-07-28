package com.leets.commitatobe.domain.commit.usecase;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.commit.domain.repository.CommitRepository;
import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.domain.tier.domain.repository.TierRepository;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
import com.leets.commitatobe.global.exception.ApiException;
import com.leets.commitatobe.global.response.code.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpService {
    private final CommitRepository commitRepository;
    private final UserRepository userRepository;
    private final TierRepository tierRepository;

    int DAILY_BONUS_EXP = 100;
    int BONUS_EXP_INCREASE = 10;

    public void calculateAndSaveExp(String githubId) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 깃허브 닉네임과 일치하는 유저를 찾을 수 없음: " + githubId));
        List<Commit> commits = commitRepository.findAllByUserOrderByCommitDateAsc(user); //사용자의 모든 커밋을 날짜 오름차순으로 불러온다.

        int consecutiveDays = user.getConsecutiveCommitDays(); //연속 커밋 일수
        LocalDateTime lastCommitDate = null; //마지막 커밋 날짜
        int totalExp = user.getExp(); //사용자의 현재 경험치
        int totalCommitCount = user.getTotalCommitCount(); //총 커밋 횟수
        int todayCommitCount = user.getTodayCommitCount(); //오늘 커밋 횟수

        for (Commit commit : commits) {//각 커밋을 반복해서 계산
            if (commit.isCalculated()) continue;//이미 계산된 커밋
            LocalDateTime commitDate = commit.getCommitDate();//커밋날짜를 가져와 시간 설정

            if (lastCommitDate != null && commitDate.isEqual(lastCommitDate.plusDays(1))) {
                // 마지막 커밋 날짜가 존재하고, 현재 커밋 날짜가 마지막 커밋 날짜의 다음 날과 같으면 연속 커밋으로 간주합니다.
                consecutiveDays++;//연속 커밋 일수 1 증가
            } else {
                if (lastCommitDate == null) { // 연속 커밋 일수는 전날 커밋이 없을 경우에 초기화
                    consecutiveDays = 0;//연속 커밋 일수 초기화
                }
            }

            totalExp += commit.calculateExp(DAILY_BONUS_EXP, consecutiveDays, BONUS_EXP_INCREASE);//총 경험치 업데이트
            totalCommitCount += commit.getCnt();//총 커밋 횟수

            if (commit.commitDateIsToday()) {
                todayCommitCount = commit.getCnt();//오늘날짜의 커밋 개수 카운트
            }

            commit.markAsCalculated();//커밋 계산 여부를 true로 해서 다음 게산에서 제외
            lastCommitDate = commitDate;//마지막 커밋날짜를 현재 커밋날짜로 업데이트
        }

        user.updateExp(totalExp);//사용자 경험치 업데이트
        Tier tier = determineTier(user.getExp());//경험치에 따른 티어 결정
        user.updateTier(tier);
        user.updateConsecutiveCommitDays(consecutiveDays);
        user.updateTotalCommitCount(totalCommitCount);
        user.updateTodayCommitCount(todayCommitCount);

        commitRepository.saveAll(commits);//변경된 커밋 정보 데이터베이스에 저장
        userRepository.save(user);//변경된 사용자 정보 데이터베이스에 저장
    }

    private Tier determineTier(Integer exp) {
        return tierRepository.findAll()
                .stream()
                .filter(tier -> tier.isValid(exp))
                .max(Comparator.comparing(Tier::getRequiredExp))
                .orElseThrow(() -> new ApiException(ErrorStatus._TIER_NOT_FOUND));
    }
}
