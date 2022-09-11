package com.example.harmony.domain.user.repository;

import com.example.harmony.domain.user.model.Family;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class FamilyRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    FamilyRepository familyRepository;

    @Test
    @DisplayName("월간 화목지수 Top 10 가족 조회")
    void findTop10ByOrderByWeeklyScoreDesc() {
        // given
        Family family1 = Family.builder()
                .weeklyScore(800)
                .build();

        Family family2 = Family.builder()
                .weeklyScore(700)
                .build();

        Family family3 = Family.builder()
                .weeklyScore(1000)
                .build();

        Family family4 = Family.builder()
                .weeklyScore(900)
                .build();

        Family family5 = Family.builder()
                .weeklyScore(600)
                .build();

        Family family6 = Family.builder()
                .weeklyScore(400)
                .build();

        Family family7 = Family.builder()
                .weeklyScore(100)
                .build();

        Family family8 = Family.builder()
                .weeklyScore(500)
                .build();

        Family family9 = Family.builder()
                .weeklyScore(300)
                .build();

        Family family10 = Family.builder()
                .weeklyScore(0)
                .build();

        Family family11 = Family.builder()
                .weeklyScore(200)
                .build();

        em.persist(family1);
        em.persist(family2);
        em.persist(family3);
        em.persist(family4);
        em.persist(family5);
        em.persist(family6);
        em.persist(family7);
        em.persist(family8);
        em.persist(family9);
        em.persist(family10);
        em.persist(family11);

        // when
        List<Family> families = familyRepository.findTop10ByOrderByWeeklyScoreDesc();

        // then
        assertEquals(10, families.size());
        assertEquals(Arrays.asList(family3, family4, family1, family2, family5, family8, family6, family9, family11, family7), families);
    }

    @Test
    @DisplayName("월간 화목지수 내림차순 조회")
    void findAllByOrderByWeeklyScoreDesc() {
        // given
        Family family1 = Family.builder()
                .weeklyScore(200)
                .build();

        Family family2 = Family.builder()
                .weeklyScore(100)
                .build();

        Family family3 = Family.builder()
                .weeklyScore(400)
                .build();

        Family family4 = Family.builder()
                .weeklyScore(300)
                .build();

        em.persist(family1);
        em.persist(family2);
        em.persist(family3);
        em.persist(family4);

        // when
        List<Family> families = familyRepository.findAllByOrderByWeeklyScoreDesc();

        // then
        assertEquals(Arrays.asList(family3, family4, family1, family2), families);
    }

    @Test
    @DisplayName("월간 화목지수 상위 10% 가족 조회")
    void findAllByFlower() {
        // given
        Family family1 = Family.builder()
                .flower(true)
                .build();

        Family family2 = Family.builder()
                .flower(true)
                .build();

        Family family3 = Family.builder()
                .flower(false)
                .build();

        Family family4 = Family.builder()
                .flower(false)
                .build();

        em.persist(family1);
        em.persist(family2);
        em.persist(family3);
        em.persist(family4);

        // when
        List<Family> flowerFamilies = familyRepository.findAllByFlower(true);

        // then
        assertEquals(Arrays.asList(family1, family2), flowerFamilies);
    }
}