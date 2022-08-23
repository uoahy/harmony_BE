package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryRequest;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.gallery.repository.ImageRepository;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    private final ScheduleRepository scheduleRepository;

    private final ImageRepository imageRepository;

    private final S3Service s3Service;

    @Transactional
    public void createGallery(Long scheduleId, GalleryRequest galleryRequest, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        List<Image> images = galleryRequest.getImageFiles().stream()
                .map(x -> new Image(s3Service.uploadFile(x)))
                .collect(Collectors.toList());
        Gallery gallery = new Gallery(galleryRequest, images, user.getFamily());
        gallery.addImages(images);
        schedule.addGallery(gallery);
        galleryRepository.save(gallery);
        imageRepository.saveAll(images);
        user.getFamily().plusScore(20);
    }
}
