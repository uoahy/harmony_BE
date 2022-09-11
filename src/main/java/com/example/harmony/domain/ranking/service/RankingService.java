package com.example.harmony.domain.ranking.service;

import com.example.harmony.domain.ranking.dto.RankingListResponse;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final FamilyRepository familyRepository;

    public RankingListResponse getRankings(User user) {
        Family myFamily = user.getFamily();
        List<Family> families = familyRepository.findAllByOrderByWeeklyScoreDesc();
        int myFamilyRanking = families.indexOf(myFamily) + 1;

        List<Family> top10 = familyRepository.findTop10ByOrderByWeeklyScoreDesc();
        List<Integer> rankings = new ArrayList<>();
        int ranking = 0;
        int score = Integer.MAX_VALUE;
        for (int i = 0; i < top10.size(); i++) {
            Family family = top10.get(i);
            if (family.getWeeklyScore() < score) {
                ranking = i + 1;
                score = family.getWeeklyScore();
            }
            rankings.add(ranking);
        }
        return new RankingListResponse(myFamily, myFamilyRanking, top10, rankings);
    }
}
