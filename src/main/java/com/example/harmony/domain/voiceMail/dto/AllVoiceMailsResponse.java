package com.example.harmony.domain.voiceMail.dto;

import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AllVoiceMailsResponse {

    private List<VoiceMailResponse> voiceMails;

    public AllVoiceMailsResponse(List<VoiceMail> voiceMails) {
        this.voiceMails = voiceMails.stream()
                .map(VoiceMailResponse::new)
                .collect(Collectors.toList());
    }
}
