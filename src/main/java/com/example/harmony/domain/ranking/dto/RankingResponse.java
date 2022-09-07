package com.example.harmony.domain.ranking.dto;

import com.example.harmony.domain.user.model.Family;
import lombok.Getter;

import java.util.List;

@Getter
public class RankingResponse {

    private int level;
    private Boolean flower;
    private int monthlyScore;
    private int raking;
    private List<Family> top10List;


<<<<<<< Updated upstream
    public RankingResponse(Family family, int raking, int level) {
        this.level = level;
        this.flower = family.isFlower();
        this.monthlyScore = family.getMonthlyScore();
        this.raking = raking;
=======
    public RankingResponse(Family family, int raking, int level, List top10List){
        this.level=level;
        this.flower=family.isFlower();
        this.monthlyScore= family.getMonthlyScore();
        this.raking= raking;
        this.top10List= top10List;
>>>>>>> Stashed changes
    }


}
