package com.example.harmony.domain.gallery.service;

import com.example.harmony.domain.gallery.dto.GalleryImageResponse;
import com.example.harmony.domain.gallery.dto.ImageAddRequest;
import com.example.harmony.domain.gallery.dto.ImageRemoveRequest;
import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.gallery.entity.Image;
import com.example.harmony.domain.gallery.repository.GalleryRepository;
import com.example.harmony.domain.gallery.repository.ImageRepository;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private final GalleryRepository galleryRepository;

    private final S3Service s3Service;

    public GalleryImageResponse getGalleryImages(Long galleryId, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (!gallery.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 이미지 조회 권한이 없습니다");
        }
        return new GalleryImageResponse(gallery.getImages());
    }

    public void addImages(Long galleryId, ImageAddRequest imageAddRequest, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (gallery.getFamily() != user.getFamily()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 사진 추가 권한이 없습니다");
        }
        if (imageRepository.countByGalleryId(galleryId) + imageAddRequest.getImageFiles().size() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지는 최대 30장까지 업로드할 수 있습니다");
        }
        List<Image> images = imageAddRequest.getImageFiles().stream()
                .map(x -> new Image(s3Service.uploadFile(x)))
                .collect(Collectors.toList());
        gallery.addImages(images);
        imageRepository.saveAll(images);
    }

    public void removeImages(Long galleryId, ImageRemoveRequest imageRemoveRequest, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (gallery.getFamily() != user.getFamily()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 사진 삭제 권한이 없습니다");
        }
        List<Image> images = imageRepository.findAllById(imageRemoveRequest.getImageIds());
        gallery.removeImages(images);
        imageRepository.deleteAllById(imageRemoveRequest.getImageIds());
        s3Service.deleteFiles(images.stream()
                .map(Image::getFilename)
                .collect(Collectors.toList()));
    }
}
