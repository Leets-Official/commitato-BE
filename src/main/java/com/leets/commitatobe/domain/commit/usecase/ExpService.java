package com.leets.commitatobe.domain.commit.usecase;

import com.leets.commitatobe.domain.commit.domain.Commit;
import com.leets.commitatobe.domain.commit.domain.repository.CommitRepository;
import com.leets.commitatobe.domain.tier.domain.Tier;
import com.leets.commitatobe.domain.tier.domain.repository.TierRepository;
import com.leets.commitatobe.domain.user.domain.User;
import com.leets.commitatobe.domain.user.domain.repository.UserRepository;
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

    public void calculateAndSaveExp(String githubId){
        User user=userRepository.findByGithubId(githubId)
                .orElseThrow(()->new UsernameNotFoundException("해당하는 깃허브 닉네임과 일치하는 유저를 찾을 수 없음: " +githubId));
        List<Commit> commits=commitRepository.findAllByUserOrderByCommitDateAsc(user);//사용자의 모든 커밋을 날짜 오름차순으로 불러온다.

        int consecutiveDays=0;//연속 커밋 일수
        LocalDateTime lastCommitDate=null;//마지막 커밋 날짜
        int totalExp=user.getExp()!=null?user.getExp():0;//사용자의 현재 경험치, user.getExp()가 null인 경우 0으로 초기화
        int dailyBonusExp=100;//데일리 보너스 경험치
        int bonusExpIncrease=10;//보너스 경험치 증가량

        for(Commit commit:commits){//각 커밋을 반복해서 계산
            if(commit.isCalculated()) continue;//이미 계산된 커밋
            LocalDateTime commitDate=commit.getCommitDate();//커밋날짜를 가져와 시간 설정

            if(lastCommitDate != null && commitDate.isEqual(lastCommitDate.plusDays(1))){
                // 마지막 커밋 날짜가 존재하고, 현재 커밋 날짜가 마지막 커밋 날짜의 다음 날과 같으면 연속 커밋으로 간주합니다.
                consecutiveDays++;//연속 커밋 일수 1 증가
            }
            else{
                consecutiveDays=0;//연속 커밋 일수 초기화
            }
            int commitExp=commit.getCnt()*5;
            int bonusExp=dailyBonusExp+consecutiveDays*bonusExpIncrease;//보너스 경험치 계산
            totalExp+=commitExp+bonusExp;//총 경험치 업데이트

            commit.isCommitCalculated(true);//커밋 계산 여부를 true로 해서 다음 게산에서 제외
            lastCommitDate=commitDate;//마지막 커밋날짜를 현재 커밋날짜로 업데이트
        }
        user.setExp(totalExp);//사용자 경험치 업데이트
        Tier tier=determineTier(user.getExp());//경험치에 따른 티어 결정
        user.setTier(tier);

        commitRepository.saveAll(commits);//변경된 커밋 정보 데이터베이스에 저장
        userRepository.save(user);//변경된 사용자 정보 데이터베이스에 저장
    }
    private Tier determineTier(Integer exp){
        if(exp>=0&&exp<=1000){
            return tierRepository.findByTierName("StupidPotato")
                    .orElseGet(()->createTier("StupidPotato"));//이 티어가 없으면 만든다.
        }
        else if(exp>=1001&&exp<=15000){
            return tierRepository.findByTierName("NormalPotato")
                    .orElseGet(()->createTier("NormalPotato"));
        }
        else{
            return tierRepository.findByTierName("DevelopPotato")
                    .orElseGet(()->createTier("DevelopPotato"));
        }
    }

    private Tier createTier(String tierName){
        return tierRepository.save(Tier.builder().tierName(tierName).build());
    }
}
