package com.example.harmony.domain.community.dto;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Getter
public class PostRequest {

    @NotBlank(message = "제목을 적어주세요.")
    @Length(max=30, message = "제목은 최대 30자까지 가능합니다.")
    private String title;

    @NotNull(message = "카테고리를 선택해주세요.")
    private String category;

    @NotBlank(message = "내용을 적어주세요.")
    @Length(max=3000, message = "내용은 최대 3000자까지 가능합니다.")
    private String content;

    private List<String> tags;

    public PostRequest(String title, String category, String content, List<String> tags) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.tags = tags;
    }

}
