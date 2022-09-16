package com.example.harmony.domain.ranking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankingResponse {

    private int ranking;

    private int level;

    private String familyName;

    private int weeklyScore;

    private int totalScore;

    private Boolean flower;
}
