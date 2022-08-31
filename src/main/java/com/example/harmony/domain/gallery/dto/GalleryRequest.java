package com.example.harmony.domain.gallery.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class GalleryRequest {

    private String date;

    @NotBlank(message = "제목은 필수 항목입니다")
    @Length(max = 30, message = "갤러리 제목은 최대 30자 이하로 적어주세요")
    private String title;

    @Size(max = 30, message = "이미지는 최대 30장까지 업로드할 수 있습니다")
    private List<MultipartFile> imageFiles;

    @NotBlank(message = "내용은 필수 항목입니다")
    @Length(max = 3000, message = "갤러리 내용은 최대 3000자 이하로 적어주세요")
    private String content;
}
