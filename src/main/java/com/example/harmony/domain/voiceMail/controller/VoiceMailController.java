package com.example.harmony.domain.voiceMail.controller;

import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.domain.voiceMail.service.VoiceMailService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class VoiceMailController {

    private final VoiceMailService voiceMailService;

    @GetMapping("/voice-mails")
    public ResponseEntity<SuccessResponse> getVoiceMails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        AllVoiceMailsResponse allVoiceMailsResponse = voiceMailService.getAllVoiceMails(userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "소리샘 조회 성공", allVoiceMailsResponse), HttpStatus.OK);
    }

    @PostMapping("/voice-mails")
    public ResponseEntity<SuccessResponse> postVoiceMail(
            @ModelAttribute VoiceMailRequest voiceMailRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        voiceMailService.createVoiceMail(voiceMailRequest, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED, "음성메시지 추가 성공"), HttpStatus.CREATED);
    }

    @DeleteMapping("/voice-mails/{voiceMailId}")
    public ResponseEntity<SuccessResponse> deleteVoiceMail(
            @PathVariable Long voiceMailId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        voiceMailService.deleteVoiceMail(voiceMailId, userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "음성메시지 삭제 성공"), HttpStatus.OK);
    }
}
