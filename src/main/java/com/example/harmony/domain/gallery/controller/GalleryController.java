package com.example.harmony.domain.gallery.controller;

import com.example.harmony.domain.gallery.dto.GalleryListResponse;
import com.example.harmony.domain.gallery.dto.GalleryRequest;
import com.example.harmony.domain.gallery.dto.ScheduleGalleryListResponse;
import com.example.harmony.domain.gallery.dto.ScheduleGalleryResponse;
import com.example.harmony.domain.gallery.service.GalleryService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping("/galleries")
    public ResponseEntity<SuccessResponse> getGalleries(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        GalleryListResponse galleryListResponse = galleryService.getGalleryList(year, month, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 조회 성공", galleryListResponse), HttpStatus.OK);
    }

    @GetMapping("/schedules/{scheduleId}/galleries")
    ResponseEntity<SuccessResponse> getScheduleGalleries(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ScheduleGalleryResponse scheduleGalleryResponse = galleryService.getScheduleGalleries(scheduleId, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "일정별 갤러리 조회 성공", scheduleGalleryResponse), HttpStatus.OK);
    }

    @GetMapping("/schedules/{scheduleId}/galleryList")
    ResponseEntity<SuccessResponse> getScheduleGalleryList(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ScheduleGalleryListResponse scheduleGalleryListResponse = galleryService.getScheduleGalleryList(scheduleId, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "일정별 갤러리 목록 조회 성공", scheduleGalleryListResponse), HttpStatus.OK);
    }

    @PostMapping("/schedules/{scheduleId}/galleries")
    ResponseEntity<SuccessResponse> postGallery(
            @PathVariable Long scheduleId,
            @ModelAttribute @Valid GalleryRequest galleryRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryService.createGallery(scheduleId, galleryRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED, "갤러리 생성 성공"), HttpStatus.CREATED);
    }

    @PutMapping("/galleries/{galleryId}")
    ResponseEntity<SuccessResponse> putGallery(
            @PathVariable Long galleryId,
            @RequestBody GalleryRequest galleryRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryService.editGallery(galleryId, galleryRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 수정 성공"), HttpStatus.OK);
    }

    @DeleteMapping("/galleries/{galleryId}")
    ResponseEntity<SuccessResponse> deleteGallery(
            @PathVariable Long galleryId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        galleryService.deleteGallery(galleryId, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "갤러리 삭제 성공"), HttpStatus.OK);
    }
}
