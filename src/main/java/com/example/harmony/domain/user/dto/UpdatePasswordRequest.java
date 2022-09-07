package com.example.harmony.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class UpdatePasswordRequest {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private String existingPassword;

    @Pattern(regexp= "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 영문, 숫자 및 특수문자를 포함한 8자 이상이어야 합니다.")
    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

}
