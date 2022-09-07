package com.example.harmony.domain.user.dto;

import com.example.harmony.domain.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyPageResponse {

    private String name;

    private String email;

    private String nickname;

    private String gender;

    public MyPageResponse(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
    }
}
