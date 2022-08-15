package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleResponse {

    private Long scheduleId;

    // TODO: enum? String?
    private String category;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> members;

    private String content;

    private Long galleryId;

    private boolean done;

    public ScheduleResponse(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.category = schedule.getCategory();
        this.title = schedule.getTitle();
        this.startDate = schedule.getStartDate();
        this.endDate = schedule.getEndDate();
        this.members = schedule.getParticipations().stream()
                .map((x) -> x.getParticipant().getRole().getRole())
                .collect(Collectors.toList());
        this.content = schedule.getContent();
        if (schedule.getGallery() != null) {
            this.galleryId = schedule.getGallery().getId();
        }
        this.done = schedule.isDone();
    }
}
