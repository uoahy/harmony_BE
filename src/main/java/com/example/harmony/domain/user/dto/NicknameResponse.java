package com.example.harmony.domain.user.dto;

import com.example.harmony.domain.user.model.User;
import lombok.Getter;

@Getter
public class NicknameResponse {

    private String nickname;

    public NicknameResponse(User user) {
        this.nickname = user.getNickname();
    }
}
