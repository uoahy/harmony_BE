package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.schedule.model.Category;
import com.example.harmony.domain.schedule.model.Schedule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MonthlyScheduleResponse {

    private List<ScheduleResponse> schedules;

    private Map<Category, Integer> counts;

    public MonthlyScheduleResponse(List<Schedule> schedules) {
        this.schedules = new ArrayList<>();
        this.counts = new HashMap<>();
        for (Schedule schedule : schedules) {
            this.schedules.add(new ScheduleResponse(schedule));
            Category category = schedule.getCategory();
            if (category != Category.PERSONAL) {
                counts.put(category, counts.getOrDefault(category, 0) + 1);
            }
        }
    }
}
