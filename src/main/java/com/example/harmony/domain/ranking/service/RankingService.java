package com.example.harmony.domain.ranking.service;

import com.example.harmony.domain.ranking.dto.RankingResponse;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class RankingService {

    private final FamilyRepository familyRepository;


    List<Family> familyList = new ArrayList<>(familyRepository.findAll(Sort.by(Sort.Direction.ASC, "monthlyScore")));

    long familyCount = familyRepository.count();//총 가족 수
    int top = (int) (familyCount * (1 / 10));//몇가족수가 나오겠지


    public RankingResponse getFamilyScore(User user) {
        Family family = familyRepository.findById(user.getFamily().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "가족 찾을 수가 없어"));
        ;
        Long familyId = user.getFamily().getId();
        int totalScore = family.getTotalScore();
        int level;
        int ranking = 0;//임시방편

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
        return new RankingResponse(family, ranking, level);
    }

}
