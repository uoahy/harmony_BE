package com.example.harmony.domain.schedule.dto;

import com.example.harmony.domain.schedule.model.Category;
import com.example.harmony.global.validator.ValueOfEnum;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class ScheduleRequest {

    @NotNull(message = "카테고리는 필수 항목입니다")
    @ValueOfEnum(enumClass = Category.class, message = "올바르지 않은 카테고리 값입니다.")
    private String category;

    @NotBlank(message = "제목은 필수 항목입니다")
    @Length(max = 30, message = "일정 제목은 최대 30자 이하로 적어주세요")
    private String title;

    @NotNull(message = "시작일은 필수 항목입니다")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수 항목입니다")
    private LocalDate endDate;

    @NotNull(message = "참여 인원은 필수 항목입니다")
    @Size(min = 1, message = "참여 인원은 최소 1명 이상 입력해주세요")
    private List<Long> memberIds;

    @NotBlank(message = "내용은 필수 항목입니다")
    @Length(max = 3000, message = "일정 내용은 최대 3000자 이하로 적어주세요")
    private String content;
}
