package com.example.harmony.domain.voiceMail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VoiceMailRequest {//생성
    private String title;
    private String from;
    private String to;
    private String sound;//파일
}
