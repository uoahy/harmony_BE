package com.example.harmony.domain.voiceMail.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VoiceMailRequest {

    private String title;

    private String from;

    private String to;

    private MultipartFile sound;
}
