package com.example.harmony.domain.gallery.dto;

import com.example.harmony.global.validator.ImageFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
public class ImageAddRequest {

    private List<@ImageFile(message = "이미지 파일만 업로드할 수 있습니다") MultipartFile> imageFiles;
}
