package com.example.harmony.domain.gallery.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GalleryCommentRequest {

    private String content;
}
