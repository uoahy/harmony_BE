package com.example.harmony.domain.voiceMail.entity;

import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.global.common.TimeStamp;
import com.example.harmony.global.s3.UploadResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class VoiceMail extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String sender;

    private String receiver;

    private String soundUrl;

    private String soundFilename;

    @ManyToOne
    private User user;

    @ManyToOne
    private Family family;

    public VoiceMail(VoiceMailRequest voiceMailRequest, UploadResponse uploadResponse, User user) {
        this.title = voiceMailRequest.getTitle();
        this.sender = voiceMailRequest.getFrom();
        this.receiver = voiceMailRequest.getTo();
        this.soundUrl = uploadResponse.getUrl();
        this.soundFilename = uploadResponse.getFilename();
        this.user = user;
        this.family = user.getFamily();
    }

    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }
}
