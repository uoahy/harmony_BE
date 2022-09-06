package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Gallery;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GalleryImageResponse {

    LocalDate date;

    private List<ImageResponse> images;

    public GalleryImageResponse(Gallery gallery) {
        this.date = gallery.getDate();
        this.images = gallery.getImages().stream()
                .map(ImageResponse::new)
                .collect(Collectors.toList());
    }
}
