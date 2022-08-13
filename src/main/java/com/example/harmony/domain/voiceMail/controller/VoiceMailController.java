package com.example.harmony.domain.voiceMail.controller;

import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.domain.voiceMail.service.VoiceMailService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class VoiceMailController {

    private final VoiceMailService voiceMailService;
    private final S3Service s3Service;

    @GetMapping("/api/voice-mails")//목록
    public ResponseEntity<SuccessResponse> allVoiceMails(@AuthenticationPrincipal User user){
        AllVoiceMailsResponse allVoiceMailsResponse = voiceMailService.allVoiceMails(user);
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK,"소리샘 조회 성공", allVoiceMailsResponse), HttpStatus.OK);
    }

    @PostMapping("/api/voice-mails")//생성
    public ResponseEntity<?> createVoiceMail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody VoiceMailRequest voiceMailRequest,
                                             @RequestPart(value = "file", required = false) MultipartFile file){
        voiceMailService.createVoiceMail(voiceMailRequest, userDetails.getUser());
        soundUrl= s3Service.uploadFile(file);
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.CREATED,"소리샘 등록 성공"),HttpStatus.OK);
    }

    @DeleteMapping("/api/voice-mails/{voiceMailId}")//삭제
    public ResponseEntity<SuccessResponse> deleteVoiceMail(@AuthenticationPrincipal User user,
                                             @PathVariable Long voiceMailId){

        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK,"소리샘 삭제 성공"),HttpStatus.OK);
    }
}
