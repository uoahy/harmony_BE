package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Image;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GalleryImageResponse {

    private List<ImageResponse> images;

    public GalleryImageResponse(List<Image> images) {
        this.images = images.stream()
                .map(ImageResponse::new)
                .collect(Collectors.toList());
    }
}
