package com.example.harmony.domain.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GalleryCommentRequest {

    @NotBlank(message = "댓글이 비어있습니다")
    @Length(max = 300, message = "댓글 내용은 최대 300자 이하로 적어주세요")
    private String content;
}
