package com.example.harmony.domain.schedule.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ScheduleRequest {

    // TODO: enum? String?
    private String category;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Long> memberIds;

    private String content;
}
