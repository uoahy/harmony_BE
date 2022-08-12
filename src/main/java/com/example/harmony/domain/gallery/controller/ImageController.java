package com.example.harmony.domain.gallery.controller;

import com.example.harmony.domain.gallery.service.ImageService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/galleries/{galleryId}/images")
    public ResponseEntity<SuccessResponse> postImages(
            @PathVariable Long galleryId,
            @RequestPart List<MultipartFile> imageFiles,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        imageService.addImages(galleryId, imageFiles, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED, "갤러리 사진 추가 성공"), HttpStatus.CREATED);
    }
}
