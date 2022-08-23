package com.example.harmony.domain.gallery.dto;

import com.example.harmony.domain.gallery.entity.Image;
import lombok.Getter;

@Getter
public class ImageResponse {

    private Long id;

    private String url;

    public ImageResponse(Image image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }
}
