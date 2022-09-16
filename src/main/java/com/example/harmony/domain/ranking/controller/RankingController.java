package com.example.harmony.domain.ranking.controller;

import com.example.harmony.domain.ranking.dto.RankingListResponse;
import com.example.harmony.domain.ranking.service.RankingService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/rankings")
    public ResponseEntity<SuccessResponse> getRankings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        RankingListResponse rankingListResponse = rankingService.getRankings(userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "랭킹 조회 성공", rankingListResponse), HttpStatus.OK);
    }
}
