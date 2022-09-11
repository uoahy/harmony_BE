package com.example.harmony.domain.ranking.dto;

import com.example.harmony.domain.user.model.Family;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RankingListResponse {

    RankingResponse family;

    List<RankingResponse> top10;

    public RankingListResponse(Family myFamily, int myFamilyRanking, List<Family> families, List<Integer> rankings) {
        this.family = RankingResponse.builder()
                .level(myFamily.getLevel())
                .flower(myFamily.isFlower())
                .weeklyScore(myFamily.getWeeklyScore())
                .totalScore(myFamily.getTotalScore())
                .ranking(myFamilyRanking)
                .build();

        this.top10 = new ArrayList<>();
        for (int i = 0; i < families.size(); i++) {
            Family family = families.get(i);
            RankingResponse rankingResponse = RankingResponse.builder()
                    .ranking(rankings.get(i))
                    .level(family.getLevel())
                    .familyName(family.getFamilyName())
                    .weeklyScore(family.getWeeklyScore())
                    .totalScore(family.getTotalScore())
                    .build();
            top10.add(rankingResponse);
        }
    }
}
