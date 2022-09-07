package com.example.harmony.domain.ranking.service;

import com.example.harmony.domain.ranking.dto.RankingResponse;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
//@EnableScheduling
@Service
public class RankingService {

    private final FamilyRepository familyRepository;


    @Scheduled(cron = "* * 6 * * MON")
//6시에 월요일마다
    int RankingMethod(int rk, Long fId) {

        List<Family> familyList = familyRepository.findAll(Sort.by(Sort.Direction.ASC, "monthlyScore"));
        if (true == familyList.contains(fId)) {
            rk = familyList.indexOf(fId);
            rk++;
        }
        return rk;
    }

    @Scheduled(cron = "* * 6 * * MON")
    List top10List() {
        List<Family> familyList = familyRepository.findAll(Sort.by(Sort.Direction.ASC, "monthlyScore"));
        List list = new ArrayList<>();
        List fl = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (Family familys : familyList) {
            map.put("familyName", familys.getFamilyName());
            map.put("score", familys.getMonthlyScore());
            fl.add(map);
        }

        for (int i = 0; i < 10; i++) {
            list.add(fl.get(i));

        }
        return list;
    }


    public RankingResponse getFamilyScore(User user) {
        Family family = familyRepository.findById(user.getFamily().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가족 찾을 수가 없어"));
        ;
        Long familyId = user.getFamily().getId();

        long familyCount = familyRepository.count();//총 가족 수
        int top = (int) (familyCount * (1 / 10));//상위 10%에 속하는 가족수

        int totalScore = family.getTotalScore();

        int level;
        int ranking = 0;


        ranking = RankingMethod(ranking, familyId);


        if (ranking < top) {
            family.setFlower();
        }

        List topList = top10List();


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
