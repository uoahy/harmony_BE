package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleListItemResponse {

    private String title;

    private List<DateGalleryPossibility> dates;

    public ScheduleListItemResponse(String title, List<LocalDate> dates, List<Gallery> galleries) {
        this.title = title;
        this.dates = new ArrayList<>();
        int i = 0;
        for (LocalDate date : dates) {
            boolean enable = false;
            if (i < galleries.size() && date.equals(galleries.get(i).getDate())) {
                enable = true;
                i++;
            }
            this.dates.add(new DateGalleryPossibility(date, enable));
        }
    }
}
