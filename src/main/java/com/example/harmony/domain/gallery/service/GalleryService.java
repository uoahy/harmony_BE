package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryListResponse;
import com.example.harmony.domain.gallery.dto.GalleryRequest;
import com.example.harmony.domain.gallery.dto.ScheduleGalleryListResponse;
import com.example.harmony.domain.gallery.dto.ScheduleGalleryResponse;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.gallery.repository.GalleryCommentRepository;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.gallery.repository.ImageRepository;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.service.FamilyService;
import com.example.harmony.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    private final ScheduleRepository scheduleRepository;

    private final ImageRepository imageRepository;

    private final GalleryCommentRepository galleryCommentRepository;

    private final S3Service s3Service;

    private final FamilyService familyService;

    public GalleryListResponse getGalleryList(int year, int month, User user) {
        LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);
        List<Schedule> schedules = scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(user.getFamily().getId(), to, from);
        schedules.sort(Comparator.comparing(Schedule::getStartDate).thenComparing(Schedule::getEndDate));
        return new GalleryListResponse(schedules);
    }

    public ScheduleGalleryResponse getScheduleGalleries(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일정별 갤러리 조회 권한이 없습니다");
        }
        return new ScheduleGalleryResponse(schedule.getTitle(), schedule.getGalleries(), user);
    }

    public ScheduleGalleryListResponse getScheduleGalleryList(Long scheduleId, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "일정별 갤러리 목록 조회 권한이 없습니다");
        }
        return new ScheduleGalleryListResponse(schedule.getGalleries());
    }

    @Transactional
    public void createGallery(Long scheduleId, GalleryRequest galleryRequest, User user) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"));
        if (!schedule.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 생성 권한이 없습니다");
        }
        List<Image> images = galleryRequest.getImageFiles().stream()
                .map(x -> new Image(s3Service.uploadFile(x)))
                .collect(Collectors.toList());
        Gallery gallery = new Gallery(galleryRequest, images, user.getFamily());
        gallery.addImages(images);
        schedule.addGallery(gallery);
        galleryRepository.save(gallery);
        imageRepository.saveAll(images);
        familyService.plusScore(user.getFamily(), 20);
    }

    public void editGallery(Long galleryId, GalleryRequest galleryRequest, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (!gallery.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 수정 권한이 없습니다");
        }
        gallery.edit(galleryRequest);
        galleryRepository.save(gallery);
    }

    @Transactional
    public void deleteGallery(Long galleryId, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (!gallery.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 삭제 권한이 없습니다");
        }
        List<String> imageFilenames = gallery.getImages().stream()
                .map(Image::getFilename)
                .collect(Collectors.toList());
        s3Service.deleteFiles(imageFilenames);
        galleryRepository.deleteById(galleryId);
        familyService.minusScore(user.getFamily(), (int) (20 + 5 * galleryCommentRepository.countByGalleryId(galleryId)));
    }
}
