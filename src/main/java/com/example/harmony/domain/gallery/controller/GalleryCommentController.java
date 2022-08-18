package com.example.harmony.domain.gallery.controller;

import com.example.harmony.domain.gallery.dto.GalleryCommentRequest;
import com.example.harmony.domain.gallery.service.GalleryCommentService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class GalleryCommentController {

    private final GalleryCommentService galleryCommentService;

    @PostMapping("/api/galleries/{galleryId}/comments")
    ResponseEntity<SuccessResponse> postComment(
            @PathVariable Long galleryId,
            @RequestBody GalleryCommentRequest galleryCommentRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryCommentService.writeGalleryComment(galleryId, galleryCommentRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 댓글 작성 성공"), HttpStatus.OK);
    }

    @PutMapping("/api/gallery-comments/{galleryCommentId}")
    ResponseEntity<SuccessResponse> putComment(
            @PathVariable Long galleryCommentId,
            @RequestBody GalleryCommentRequest galleryCommentRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryCommentService.editGalleryComment(galleryCommentId, galleryCommentRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 댓글 수정 성공"), HttpStatus.OK);
    }

    @DeleteMapping("/api/gallery-comments/{galleryCommentId}")
    ResponseEntity<SuccessResponse> deleteComment(
            @PathVariable Long galleryCommentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryCommentService.deleteGalleryComment(galleryCommentId, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 댓글 삭제 성공"), HttpStatus.OK);
    }
}
