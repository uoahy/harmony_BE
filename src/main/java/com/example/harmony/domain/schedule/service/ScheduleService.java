package com.example.harmony.domain.schedule.service;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.schedule.entity.Participation;
import com.example.harmony.domain.schedule.entity.Schedule;
import com.example.harmony.domain.schedule.repository.ParticipationRepository;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    public MonthlyScheduleResponse getMonthlySchedule(int year, int month, User user) {
        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(user.getFamily().getId(), to, from);
        return new MonthlyScheduleResponse(schedules);
    }

    @Transactional
    public void registerSchedule(ScheduleRequest scheduleRequest, User user) {
        Schedule schedule = scheduleRepository.save(new Schedule(scheduleRequest, user.getFamily()));
        List<User> participants = userRepository.findAllById(scheduleRequest.getMemberIds());
        List<Participation> participations = participants.stream()
                .map(x -> new Participation(schedule, x))
                .collect(Collectors.toList());
        participationRepository.saveAll(participations);
    }
}
