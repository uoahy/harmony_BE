package com.example.harmony.domain.voiceMail.entity;

import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.global.common.TimeStamp;
import com.example.harmony.global.s3.UploadResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "sender")
    private String from;

    @Column(name = "receiver")
    private String to;

    private String soundUrl;

    private String soundFileName;

    @ManyToOne
    private User user;

    @ManyToOne
    private Family family;

    public VoiceMail (User user, Family family, VoiceMailRequest voiceMailRequest, UploadResponse uploadResponse){
        this.user= user;
        this.family= family;
        this.title= voiceMailRequest.getTitle();
        this.from= voiceMailRequest.getFrom();
        this.to= voiceMailRequest.getTo();
        this.soundUrl= uploadResponse.getUrl();
        this.soundFileName= uploadResponse.getFilename();
    }
}
