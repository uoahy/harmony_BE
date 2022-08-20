package com.example.harmony.domain.gallery.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
public class GalleryCommentRequest {

    @NotBlank(message = "댓글이 비어있습니다")
    @Length(max = 300, message = "댓글 내용은 최대 300자 이하로 적어주세요")
    private String content;
}
