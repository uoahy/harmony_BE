package com.example.harmony.domain.gallery.controller;

import com.example.harmony.domain.gallery.dto.ImageRemoveRequest;
import com.example.harmony.domain.gallery.service.ImageService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/api/gallery/{galleryId}/images")
    public ResponseEntity<SuccessResponse> deleteImages(
            @PathVariable Long galleryId,
            @RequestBody ImageRemoveRequest imageRemoveRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        imageService.removeImages(galleryId, imageRemoveRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 사진 삭제 성공"), HttpStatus.OK);
    }
}
