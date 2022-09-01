package com.example.harmony.domain.community.dto;

import com.example.harmony.global.validator.ImageFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class PostRequest {

    @NotBlank(message = "제목을 적어주세요.")
    @Length(max=20, message = "제목은 최대 20자까지 가능합니다.")
    private String title;

    @NotNull(message = "카테고리를 선택해주세요.")
    private String category;

    @NotBlank(message = "내용을 적어주세요.")
    @Length(max=3000, message = "내용은 최대 3000자까지 가능합니다.")
    private String content;

    @Nullable
    private List<String> tags;

    @ImageFile
    @Nullable
    private MultipartFile image;

    public PostRequest(String title, String category, String content, List<String> tags, MultipartFile image) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
        this.image = image;
    }

}
