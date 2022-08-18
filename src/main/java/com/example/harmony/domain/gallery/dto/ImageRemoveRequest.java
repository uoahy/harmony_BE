package com.example.harmony.domain.gallery.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ImageRemoveRequest {

    List<Long> imageIds;
}
