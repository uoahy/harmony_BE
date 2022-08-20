package com.example.harmony.domain.gallery.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ImageRemoveRequest {

    List<Long> imageIds;
}
