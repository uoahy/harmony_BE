package com.example.harmony.domain.schedule.controller;

import com.example.harmony.domain.schedule.dto.MonthlyScheduleResponse;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.schedule.service.ScheduleService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/api/schedules")
    public ResponseEntity<SuccessResponse> getSchedules(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MonthlyScheduleResponse monthlyScheduleResponse = scheduleService.getMonthlySchedule(year, month, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "일정 조회 성공", monthlyScheduleResponse), HttpStatus.OK);
    }

    @PostMapping("/api/schedules")
    public ResponseEntity<SuccessResponse> postSchedule(@RequestBody ScheduleRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        scheduleService.registerSchedule(request, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED, "일정 등록 성공"), HttpStatus.CREATED);
    }
}
