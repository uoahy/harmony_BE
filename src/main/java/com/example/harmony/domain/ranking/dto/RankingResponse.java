package com.example.harmony.domain.ranking.dto;

import com.example.harmony.domain.user.model.Family;
import lombok.Getter;

@Getter
public class RankingResponse {

    private int level;
    private Boolean flower;
    private int monthlyScore;
    private int raking;


    public RankingResponse(Family family, int raking, int level) {
        this.level = level;
        this.flower = family.isFlower();
        this.monthlyScore = family.getMonthlyScore();
        this.raking = raking;
    }


}
