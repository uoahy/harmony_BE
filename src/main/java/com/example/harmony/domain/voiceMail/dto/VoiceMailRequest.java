package com.example.harmony.domain.voiceMail.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class VoiceMailRequest {

    private String title;

    private String from;

    private String to;

    private MultipartFile sound;
}
