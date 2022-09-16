package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.user.model.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleGalleryResponse {

    private String scheduleTitle;

    private List<GalleryResponse> galleries;

    public ScheduleGalleryResponse(String scheduleTitle, List<Gallery> galleries, User user) {
        this.scheduleTitle = scheduleTitle;
        this.galleries = galleries.stream()
                .map(x -> new GalleryResponse(x, user))
                .collect(Collectors.toList());
    }
}
