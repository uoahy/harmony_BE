package com.example.harmony.domain.gallery.service;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private final GalleryRepository galleryRepository;

    private final S3Service s3Service;

    public void addImages(Long galleryId, List<MultipartFile> imageFiles, User user) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "갤러리를 찾을 수 없습니다"));
        if (gallery.getFamily() != user.getFamily()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "갤러리 사진 추가 권한이 없습니다");
        }
        if (imageRepository.countByGalleryId(galleryId) + imageFiles.size() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지는 최대 30장까지 업로드할 수 있습니다");
        }
        List<Image> images = imageFiles.stream()
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
        for (Image image : images) {
            if (image.getGallery() != gallery) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지가 속해있는 갤러리가 서로 다릅니다");
            }
        }
        gallery.removeImages(images);
        imageRepository.deleteAllById(imageRemoveRequest.getImageIds());
        s3Service.deleteFiles(images.stream()
                .map(Image::getFilename)
                .collect(Collectors.toList()));
    }
}
