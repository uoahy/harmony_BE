package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.schedule.model.Schedule;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleListResponse {

    private List<ScheduleListItemResponse> schedules;

    public ScheduleListResponse(List<Schedule> schedules) {
        this.schedules = new ArrayList<>();
        for (Schedule schedule : schedules) {
            List<LocalDate> dates = schedule.getStartDate().datesUntil(schedule.getEndDate().plusDays(1))
                    .collect(Collectors.toList());
            List<Gallery> galleries = schedule.getGalleries();

            if (dates.size() != galleries.size()) {
                this.schedules.add(new ScheduleListItemResponse(schedule.getTitle(), dates, galleries));
            }
        }
    }
}
