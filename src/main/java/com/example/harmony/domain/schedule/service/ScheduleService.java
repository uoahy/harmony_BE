package com.example.harmony.domain.schedule.service;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.dto.ScheduleListResponse;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.schedule.model.Participation;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.schedule.repository.ParticipationRepository;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.UserRepository;
import com.example.harmony.domain.user.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ParticipationRepository participationRepository;

    private final UserRepository userRepository;

    private final FamilyService familyService;

    public MonthlyScheduleResponse getMonthlySchedule(int year, int month, User user) {
        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(user.getFamily().getId(), to, from);
        return new MonthlyScheduleResponse(schedules);
    }

    public ScheduleListResponse getSchedulesDates(int year, int month, User user) {
        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(user.getFamily().getId(), to, from);
        schedules.sort(Comparator.comparing(Schedule::getStartDate).thenComparing(Schedule::getEndDate));
        return new ScheduleListResponse(schedules);
    }

    @Transactional
    public void registerSchedule(ScheduleRequest scheduleRequest, User user) {
        Schedule schedule = scheduleRepository.save(new Schedule(scheduleRequest, user.getFamily()));
        List<User> participants = userRepository.findAllById(scheduleRequest.getMemberIds());
        List<Participation> participations = participants.stream()
                .map(x -> new Participation(schedule, x))
                .collect(Collectors.toList());
        participationRepository.saveAll(participations);
        if (schedule.isDone() && participants.size() >= 2) {
            familyService.plusScore(schedule.getFamily(), 10);
        }
    }

    @Transactional
    public void modifySchedule(Long scheduleId, ScheduleRequest scheduleRequest, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일정 수정 권한이 없습니다");
        }
        if (!schedule.isDone()) {
            List<User> participants = userRepository.findAllById(scheduleRequest.getMemberIds());
            List<Participation> participations = participants.stream()
                    .map(x -> new Participation(schedule, x))
                    .collect(Collectors.toList());
            participationRepository.deleteAll(schedule.getParticipations());
            schedule.modify(scheduleRequest, participations);
            participationRepository.saveAll(participations);
        } else {
            schedule.modify(scheduleRequest, null);
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일정 삭제 권한이 없습니다");
        }
        if (schedule.getGalleries().size() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일정에 연동된 갤러리가 있습니다");
        }
        scheduleRepository.deleteById(scheduleId);

        if (schedule.isDone() && schedule.getParticipations().size() >= 2) {
            familyService.minusScore(user.getFamily(), 10);
        }
    }

    @Transactional
    public void setScheduleDone(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일정 완료여부 설정 권한이 없습니다");
        }
        schedule.setDone();

        if (schedule.getParticipations().size() >= 2) {
            if (schedule.isDone()) {
                familyService.plusScore(schedule.getFamily(), 10);
            } else {
                familyService.minusScore(schedule.getFamily(), 10);
            }
        }
    }
}
