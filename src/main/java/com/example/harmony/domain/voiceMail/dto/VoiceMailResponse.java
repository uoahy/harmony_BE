package com.example.harmony.domain.voiceMail.dto;

import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VoiceMailResponse {

    private Long voiceMailId;

    private String title;

    private String from;

    private String to;

    private String soundUrl;

    private LocalDateTime createdAt;

    public VoiceMailResponse(VoiceMail voiceMail) {
        this.voiceMailId = voiceMail.getId();
        this.title = voiceMail.getTitle();
        this.from = voiceMail.getSender();
        this.to = voiceMail.getReceiver();
        this.soundUrl = voiceMail.getSoundUrl();
        this.createdAt = voiceMail.getCreatedAt();
    }
}
