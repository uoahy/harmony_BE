package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.schedule.model.Schedule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonthlyScheduleResponse {

    private List<ScheduleResponse> schedules;

    private int eatCount;

    private int tripCount;

    private int cookCount;

    private int cleanCount;

    public MonthlyScheduleResponse(List<Schedule> schedules) {
        this.schedules = new ArrayList<>();
        this.eatCount = 0;
        this.tripCount = 0;
        this.cookCount = 0;
        this.cleanCount = 0;

        for (Schedule schedule : schedules) {
            this.schedules.add(new ScheduleResponse(schedule));
            switch (schedule.getCategory()) {
                case EAT_OUT:
                    this.eatCount++;
                case TRIP:
                    this.tripCount++;
                case COOK:
                    this.cookCount++;
                case CLEAN:
                    this.cleanCount++;
            }
        }
    }
}
