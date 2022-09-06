package com.example.harmony.domain.schedule.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DateGalleryPossibility {

    private LocalDate date;

    private boolean enable;

    public DateGalleryPossibility(LocalDate date, boolean enable) {
        this.date = date;
        this.enable = enable;
    }
}
