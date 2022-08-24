package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleGalleryListResponse {

    private List<GalleryResponse> galleries;

    public ScheduleGalleryListResponse(List<Gallery> galleries) {
        this.galleries = galleries.stream()
                .map(GalleryResponse::new)
                .collect(Collectors.toList());
    }
}
