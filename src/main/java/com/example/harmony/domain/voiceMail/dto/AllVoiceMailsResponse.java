package com.example.harmony.domain.voiceMail.dto;

import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class AllVoiceMailsResponse {
    private Long voiceMailId;
    private String title;
    private String from;
    private String to;
    private LocalDateTime createdAt;
    private String soundUrl;

    public AllVoiceMailsResponse(VoiceMail voiceMail){
        this.voiceMailId=voiceMail.getId();
        this.title=voiceMail.getTitle();
        this.from=voiceMail.getFrom();
        this.to=voiceMail.getTo();
        this.createdAt=voiceMail.getCreatedAt();
        this.soundUrl=voiceMail.getSoundUrl();
    }
}
