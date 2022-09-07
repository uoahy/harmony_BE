package com.example.harmony.domain.ranking.service;

import com.example.harmony.domain.ranking.dto.RankingResponse;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RequiredArgsConstructor
@EnableScheduling
@Service
public class RankingService {

    private final FamilyRepository familyRepository;


    List<Family> familyList = familyRepository.findAll(Sort.by(Sort.Direction.ASC, "monthlyScore"));

    long familyCount = familyRepository.count();//총 가족 수
    int top = (int) (familyCount * (1 / 10));//몇가족수가 나오겠지

    @Scheduled(cron = "* * 5 1 * *")
    int RankingMethod(int rk, Long fId) {
        if (true == familyList.contains(fId)) {
            rk = familyList.indexOf(fId);
            rk++;//이 값이 top보다 낮다 하면 플라워에 ture를 주는!!!! 그런식?
        }
        return rk;
    }

    @Scheduled(cron = "* * 5 1 * *")
    List top10List(List List) {
        for (int i = 0; i < 10; i++) {
            List.add(familyList.get(i));
        }
        return List;
    }


    public RankingResponse getFamilyScore(User user) {
        Family family = familyRepository.findById(user.getFamily().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가족 찾을 수가 없어"));
        ;
        Long familyId = user.getFamily().getId();
        int totalScore = family.getTotalScore();

        int level;
        int ranking = 0;
        List topList = null;//임시방편


        ranking = RankingMethod(ranking, familyId);


        if (ranking < top) {
            family.setFlower();
        }
        topList = top10List(topList);

        if (totalScore >= 3000) {
            level = 4;
        } else if (totalScore >= 2999 && totalScore <= 770) {
            level = 3;
        } else if (totalScore >= 769 && totalScore <= 220) {
            level = 2;
        } else if (totalScore >= 219 && totalScore <= 55) {
            level = 1;
        } else {
            level = 0;
        }

        return new RankingResponse(family, ranking, level, topList);
    }

}
