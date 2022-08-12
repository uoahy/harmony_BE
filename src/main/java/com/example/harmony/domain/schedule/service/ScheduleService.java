package com.example.harmony.domain.schedule.service;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.entity.Schedule;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public MonthlyScheduleResponse getMonthlySchedule(int year, int month, User user) {
        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(user.getFamily().getId(), to, from);
        return new MonthlyScheduleResponse(schedules);
    }
}
