package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryListItemResponse;
import com.example.harmony.domain.gallery.dto.GalleryListResponse;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.gallery.repository.GalleryCommentRepository;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.gallery.repository.ImageRepository;
import com.example.harmony.domain.schedule.model.Category;
import com.example.harmony.domain.schedule.model.Schedule;
import com.example.harmony.domain.schedule.repository.ScheduleRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GalleryServiceTest {

    @InjectMocks
    GalleryService galleryService;

    @Mock
    GalleryRepository galleryRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ImageRepository imageRepository;

    @Mock
    GalleryCommentRepository galleryCommentRepository;

    @Mock
    S3Service s3Service;

    @Nested
    @DisplayName("갤러리 목록 조회")
    class GetGalleryList {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                int year = 2022;
                int month = 8;

                LocalDate from = LocalDate.of(year, month, 1).minusDays(1);
                LocalDate to = LocalDate.of(year, month, 1).plusMonths(1);

                Long familyId = 1L;
                Family family = Family.builder()
                        .id(familyId)
                        .build();

                User user = User.builder()
                        .family(family)
                        .build();

                Image image = Image.builder().build();

                Gallery gallery1 = Gallery.builder()
                        .images(Arrays.asList(image, image, image))
                        .build();

                Gallery gallery2 = Gallery.builder()
                        .images(Arrays.asList(image, image))
                        .build();

                Gallery gallery3 = Gallery.builder()
                        .images(Arrays.asList(image))
                        .build();

                Schedule schedule1 = Schedule.builder()
                        .id(1L)
                        .category(Category.ETC)
                        .startDate(LocalDate.of(2022, 8, 15))
                        .endDate(LocalDate.of(2022, 8, 15))
                        .galleries(Arrays.asList(gallery1))
                        .build();

                Schedule schedule2 = Schedule.builder()
                        .id(2L)
                        .category(Category.TRIP)
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 15))
                        .galleries(Arrays.asList(gallery2))
                        .build();

                Schedule schedule3 = Schedule.builder()
                        .id(3L)
                        .category(Category.EAT_OUT)
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .galleries(Arrays.asList(gallery3))
                        .build();

                Schedule schedule4 = Schedule.builder()
                        .id(4L)
                        .category(Category.ETC)
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .galleries(Collections.emptyList())
                        .build();

                List<Schedule> schedules = Arrays.asList(schedule1, schedule2, schedule3, schedule4);

                when(scheduleRepository.findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(familyId, to, from))
                        .thenReturn(schedules);

                // when
                GalleryListResponse galleryListResponse = galleryService.getGalleryList(year, month, user);

                // then
                assertEquals(3, galleryListResponse.getGalleries().size());
                assertEquals(Arrays.asList(3L, 2L, 1L), galleryListResponse.getGalleries().stream().map(GalleryListItemResponse::getScheduleId).collect(Collectors.toList()));
                assertEquals(Arrays.asList(1, 2, 3), galleryListResponse.getGalleries().stream().map(GalleryListItemResponse::getCount).collect(Collectors.toList()));
            }
        }
    }
}