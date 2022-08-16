package com.example.harmony.domain.schedule.service;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Nested
    @DisplayName("일정 등록")
    class RegisterSchedule {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("종료일이 시작일보다 과거")
            void endDate_is_before_startDate() {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 7))
                        .build();

                Family family = Family.builder().build();

                User user = User.builder()
                        .family(family)
                        .build();

                scheduleService = new ScheduleService(scheduleRepository, participationRepository, userRepository);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> scheduleService.registerSchedule(scheduleRequest, user));

                // then
                assertEquals("400 BAD_REQUEST \"종료일은 시작일 이후여야 합니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("일정 참여인원 중 가족구성원이 아닌 유저 포함")
            void not_family_members() {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("TRIP")
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .build();

                Family family1 = Family.builder().build();

                User user1 = User.builder()
                        .family(family1)
                        .build();

                Family family2 = Family.builder().build();

                User user2 = User.builder()
                        .family(family2)
                        .build();

                Schedule schedule = new Schedule(scheduleRequest, user1.getFamily());

                when(scheduleRepository.save(any(Schedule.class)))
                        .thenReturn(schedule);

                when(userRepository.findAllById(scheduleRequest.getMemberIds()))
                        .thenReturn(new ArrayList<>(Arrays.asList(user1, user2)));

                scheduleService = new ScheduleService(scheduleRepository, participationRepository, userRepository);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> scheduleService.registerSchedule(scheduleRequest, user1));

                // then
                assertEquals("400 BAD_REQUEST \"가족 구성원만 일정에 참여할 수 있습니다\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("종료일이 현재 이후")
            void endDate_is_after_or_equals_now() {
                // given
                LocalDate today = LocalDate.now();

                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("TRIP")
                        .startDate(today)
                        .endDate(today)
                        .build();

                Family family = Family.builder().build();

                User user1 = User.builder()
                        .family(family)
                        .build();

                User user2 = User.builder()
                        .family(family)
                        .build();

                Schedule schedule = new Schedule(scheduleRequest, user1.getFamily());

                when(scheduleRepository.save(any(Schedule.class)))
                        .thenReturn(schedule);

                when(userRepository.findAllById(scheduleRequest.getMemberIds()))
                        .thenReturn(new ArrayList<>(Arrays.asList(user1, user2)));

                scheduleService = new ScheduleService(scheduleRepository, participationRepository, userRepository);

                // when
                assertDoesNotThrow(() -> scheduleService.registerSchedule(scheduleRequest, user1));

                // then
                assertFalse(schedule.isDone());
            }

            @Test
            @DisplayName("종료일이 현재보다 과거")
            void endDate_is_before_now() {
                // given
                LocalDate yesterday = LocalDate.now().minusDays(1);

                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("TRIP")
                        .startDate(yesterday)
                        .endDate(yesterday)
                        .build();

                Family family = Family.builder().build();

                User user1 = User.builder()
                        .family(family)
                        .build();

                User user2 = User.builder()
                        .family(family)
                        .build();

                Schedule schedule = new Schedule(scheduleRequest, user1.getFamily());

                when(scheduleRepository.save(any(Schedule.class)))
                        .thenReturn(schedule);

                when(userRepository.findAllById(scheduleRequest.getMemberIds()))
                        .thenReturn(new ArrayList<>(Arrays.asList(user1, user2)));

                scheduleService = new ScheduleService(scheduleRepository, participationRepository, userRepository);

                // when
                assertDoesNotThrow(() -> scheduleService.registerSchedule(scheduleRequest, user1));

                // then
                assertTrue(schedule.isDone());
            }
        }
    }
}