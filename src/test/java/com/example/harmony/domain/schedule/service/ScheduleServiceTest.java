package com.example.harmony.domain.schedule.service;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.model.Category;
import com.example.harmony.domain.schedule.model.Participation;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.schedule.repository.ParticipationRepository;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.RoleEnum;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    ScheduleService scheduleService;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ParticipationRepository participationRepository;

    @Mock
    UserRepository userRepository;

    @Nested
    @DisplayName("월별 일정 조회")
    class GetMonthlySchedule {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                int year = 2022;
                int month = 8;

                LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
                LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);

                Long familyId = 1L;
                Family family = Family.builder()
                        .id(familyId)
                        .build();

                User user = User.builder()
                        .family(family)
                        .role(RoleEnum.FIRST)
                        .build();

                Participation participation1 = Participation.builder()
                        .participant(user)
                        .build();

                Schedule schedule1 = Schedule.builder()
                        .category(Category.TRIP)
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .participations(new ArrayList<>(Arrays.asList(participation1)))
                        .build();

                Schedule schedule2 = Schedule.builder()
                        .category(Category.ETC)
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .participations(new ArrayList<>(Arrays.asList(participation1)))
                        .build();

                List<Schedule> schedules = new ArrayList<>(Arrays.asList(schedule1, schedule2));

                when(scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(familyId, to, from))
                        .thenReturn(schedules);

                scheduleService = new ScheduleService(scheduleRepository, participationRepository, userRepository);

                // when
                MonthlyScheduleResponse monthlyScheduleResponse = scheduleService.getMonthlySchedule(year, month, user);

                // then
                assertEquals(0, monthlyScheduleResponse.getEatCount());
                assertEquals(1, monthlyScheduleResponse.getTripCount());
                assertEquals(0, monthlyScheduleResponse.getCookCount());
                assertEquals(0, monthlyScheduleResponse.getCleanCount());
                assertEquals(1, monthlyScheduleResponse.getEtcCount());
            }
        }
    }
}