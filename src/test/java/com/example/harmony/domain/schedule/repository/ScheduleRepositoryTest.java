package com.example.harmony.domain.schedule.repository;

import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.user.entity.Family;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Test
    @DisplayName("월별 일정 조회")
    void findAllByFamilyIdAndStartDateBeforeAndEndDateAfter() {
        // given
        Family family = Family.builder().build();

        Schedule schedule1 = Schedule.builder()
                .startDate(LocalDate.of(2022, 7, 31))
                .endDate(LocalDate.of(2022, 7, 31))
                .family(family)
                .build();

        Schedule schedule2 = Schedule.builder()
                .startDate(LocalDate.of(2022, 7, 31))
                .endDate(LocalDate.of(2022, 8, 1))
                .family(family)
                .build();

        Schedule schedule3 = Schedule.builder()
                .startDate(LocalDate.of(2022, 8, 15))
                .endDate(LocalDate.of(2022, 8, 15))
                .family(family)
                .build();

        Schedule schedule4 = Schedule.builder()
                .startDate(LocalDate.of(2022, 8, 31))
                .endDate(LocalDate.of(2022, 9, 1))
                .family(family)
                .build();

        Schedule schedule5 = Schedule.builder()
                .startDate(LocalDate.of(2022, 9, 1))
                .endDate(LocalDate.of(2022, 9, 1))
                .family(family)
                .build();

        em.persist(family);
        em.persist(schedule1);
        em.persist(schedule2);
        em.persist(schedule3);
        em.persist(schedule4);
        em.persist(schedule5);

        int year = 2022;
        int month = 8;

        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);


        // when
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(family.getId(), to, from);

        // then
        assertEquals(Arrays.asList(schedule2, schedule3, schedule4), schedules);
    }
}