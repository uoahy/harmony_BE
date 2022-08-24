package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.schedule.model.Schedule;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GalleryListResponse {

    private List<GalleryListItemResponse> galleries;

    public GalleryListResponse(List<Schedule> schedules) {
        this.galleries = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (!schedule.getGalleries().isEmpty()) {
                this.galleries.add(new GalleryListItemResponse(schedule));
            }
        }
    }
}
