package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryCommentRequest;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.repository.GalleryCommentRepository;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GalleryCommentServiceTest {

    @InjectMocks
    GalleryCommentService galleryCommentService;

    @Mock
    GalleryRepository galleryRepository;

    @Mock
    GalleryCommentRepository galleryCommentRepository;

    @Nested
    @DisplayName("갤러리 댓글 작성")
    class WriteGalleryComment {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("존재하지않는 갤러리")
            void gallery_not_found() {
                // given
                Long galleryId = -1L;

                GalleryCommentRequest galleryCommentRequest = GalleryCommentRequest.builder().build();

                User user = User.builder().build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> galleryCommentService.writeGalleryComment(galleryId, galleryCommentRequest, user));

                // then
                assertEquals("404 NOT_FOUND \"갤러리를 찾을 수 없습니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("가족구성원이 아닌 유저가 갤러리 댓글 작성 시도")
            void user_is_not_family_member() {
                // given
                Long galleryId = 1L;

                Family family1 = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family1)
                        .build();

                GalleryCommentRequest galleryCommentRequest = GalleryCommentRequest.builder().build();

                Family family2 = Family.builder()
                        .id(2L)
                        .build();

                User user = User.builder()
                        .family(family2)
                        .build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> galleryCommentService.writeGalleryComment(galleryId, galleryCommentRequest, user));

                // then
                assertEquals("403 FORBIDDEN \"댓글 작성 권한이 없습니다\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                Long galleryId = 1L;

                Family family = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family)
                        .comments(new ArrayList<>())
                        .build();

                GalleryCommentRequest galleryCommentRequest = GalleryCommentRequest.builder().build();

                User user = User.builder()
                        .family(family)
                        .build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                // when & then
                assertDoesNotThrow(() -> galleryCommentService.writeGalleryComment(galleryId, galleryCommentRequest, user));
            }
        }
    }
}