package com.example.harmony.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateInfoRequest {

    private String gender;

    private String nickname;

    private String updateFor;

}
