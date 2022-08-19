package com.example.harmony.domain.gallery.dto;

import com.example.harmony.global.validator.ImageFile;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
public class ImageAddRequest {

    List<@ImageFile(message = "이미지 파일만 업로드할 수 있습니다") MultipartFile> imageFiles;
}
