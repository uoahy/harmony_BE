package com.example.harmony.domain.ranking.Scheduler;

import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RankingScheduler {

    private final FamilyRepository familyRepository;

    @Scheduled(cron = "* * 6 * * MON")
    public void setFlowers() {
        List<Family> flowerFamilies = familyRepository.findAllByFlower(true);
        for (Family flowerFamily : flowerFamilies) {
            flowerFamily.setFlower(false);
        }
        familyRepository.saveAll(flowerFamilies);

        List<Family> families = familyRepository.findAllByOrderByWeeklyScoreDesc();
        for (int i = 0; i < families.size() / 10; i++) {
            families.get(i).setFlower(true);
        }
        familyRepository.saveAll(families);

        initWeeklyScores();
    }

    public void initWeeklyScores() {
        List<Family> families = familyRepository.findAll();
        for (Family family : families) {
            family.initWeeklyScore();
        }
        familyRepository.saveAll(families);
    }
}
