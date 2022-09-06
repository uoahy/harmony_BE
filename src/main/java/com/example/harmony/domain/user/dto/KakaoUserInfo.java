package com.example.harmony.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfo {

    private String email;

    private String name;

    public KakaoUserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
