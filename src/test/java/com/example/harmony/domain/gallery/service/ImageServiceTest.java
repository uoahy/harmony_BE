package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.ImageAddRequest;
import com.example.harmony.domain.gallery.dto.ImageRemoveRequest;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.gallery.repository.ImageRepository;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Mock
    ImageRepository imageRepository;

    @Mock
    GalleryRepository galleryRepository;

    @Mock
    S3Service s3Service;

    @Nested
    @DisplayName("갤러리 이미지 추가")
    class AddImages {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("존재하지않는 갤러리")
            void gallery_not_found() {
                // given
                Long galleryId = -1L;

                ImageAddRequest imageAddRequest = ImageAddRequest.builder().build();

                User user = User.builder().build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.addImages(galleryId, imageAddRequest, user));

                // then
                assertEquals("404 NOT_FOUND \"갤러리를 찾을 수 없습니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("가족구성원이 아닌 유저가 갤러리 이미지 추가 시도")
            void user_is_not_family_member() {
                // given
                Long galleryId = 1L;

                Family family1 = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family1)
                        .build();

                Family family2 = Family.builder()
                        .id(2L)
                        .build();

                User user = User.builder()
                        .family(family2)
                        .build();

                ImageAddRequest imageAddRequest = ImageAddRequest.builder().build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.addImages(galleryId, imageAddRequest, user));

                // then
                assertEquals("403 FORBIDDEN \"갤러리 사진 추가 권한이 없습니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("이미지 최대 30장 업로드 제한")
            void maximum_numbers_of_images() {
                // given
                Long galleryId = 1L;

                Family family = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family)
                        .build();

                User user = User.builder()
                        .family(family)
                        .build();

                MockMultipartFile imageFile = new MockMultipartFile("imageFiles", (byte[]) null);

                List<MultipartFile> imageFiles = Arrays.asList(imageFile);

                ImageAddRequest imageAddRequest = ImageAddRequest.builder()
                        .imageFiles(imageFiles)
                        .build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                when(imageRepository.countByGalleryId(galleryId))
                        .thenReturn(30L);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.addImages(galleryId, imageAddRequest, user));

                // then
                assertEquals("400 BAD_REQUEST \"이미지는 최대 30장까지 업로드할 수 있습니다\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                Long galleryId = 1L;

                Family family = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family)
                        .images(new ArrayList<>())
                        .build();

                User user = User.builder()
                        .family(family)
                        .build();

                MockMultipartFile imageFile = new MockMultipartFile("imageFiles", (byte[]) null);

                List<MultipartFile> imageFiles = Arrays.asList(imageFile);

                ImageAddRequest imageAddRequest = ImageAddRequest.builder()
                        .imageFiles(imageFiles)
                        .build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                when(imageRepository.countByGalleryId(galleryId))
                        .thenReturn(20L);

                when(s3Service.uploadFile(imageFile))
                        .thenReturn(new UploadResponse("image url", "image filename"));

                // when & then
                assertDoesNotThrow(() -> imageService.addImages(galleryId, imageAddRequest, user));
            }
        }
    }

    @Nested
    @DisplayName("갤러리 이미지 삭제")
    class RemoveImages {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("존재하지않는 갤러리")
            void gallery_not_found() {
                // given
                Long galleryId = -1L;

                ImageRemoveRequest imageRemoveRequest = ImageRemoveRequest.builder().build();

                User user = User.builder().build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.removeImages(galleryId, imageRemoveRequest, user));

                // then
                assertEquals("404 NOT_FOUND \"갤러리를 찾을 수 없습니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("가족구성원이 아닌 유저가 갤러리 이미지 추가 시도")
            void user_is_not_family_member() {
                // given
                Long galleryId = 1L;

                Family family1 = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family1)
                        .build();

                ImageRemoveRequest imageRemoveRequest = ImageRemoveRequest.builder().build();

                Family family2 = Family.builder()
                        .id(2L)
                        .build();

                User user = User.builder()
                        .family(family2)
                        .build();

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.removeImages(galleryId, imageRemoveRequest, user));

                // then
                assertEquals("403 FORBIDDEN \"갤러리 사진 삭제 권한이 없습니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("갤러리에 존재하지않는 이미지")
            void image_not_exist() {
                // given
                Long galleryId = 1L;

                Family family = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family)
                        .build();

                ImageRemoveRequest imageRemoveRequest = ImageRemoveRequest.builder().build();

                User user = User.builder()
                        .family(family)
                        .build();

                Image image = Image.builder().build();

                List<Image> images = Arrays.asList(image);

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                when(imageRepository.findAllById(imageRemoveRequest.getImageIds()))
                        .thenReturn(images);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> imageService.removeImages(galleryId, imageRemoveRequest, user));

                // then
                assertEquals("400 BAD_REQUEST \"갤러리에 없는 이미지입니다\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                Long galleryId = 1L;

                Family family = Family.builder()
                        .id(1L)
                        .build();

                Gallery gallery = Gallery.builder()
                        .family(family)
                        .images(new ArrayList<>())
                        .build();

                ImageRemoveRequest imageRemoveRequest = ImageRemoveRequest.builder().build();

                User user = User.builder()
                        .family(family)
                        .build();

                Image image = Image.builder()
                        .gallery(gallery)
                        .build();

                List<Image> images = Arrays.asList(image);

                when(galleryRepository.findById(galleryId))
                        .thenReturn(Optional.of(gallery));

                when(imageRepository.findAllById(imageRemoveRequest.getImageIds()))
                        .thenReturn(images);

                // when & then
                assertDoesNotThrow(() -> imageService.removeImages(galleryId, imageRemoveRequest, user));
            }
        }
    }
}