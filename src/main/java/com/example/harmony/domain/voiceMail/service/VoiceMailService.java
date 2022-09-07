package com.example.harmony.domain.voiceMail.service;

import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import com.example.harmony.domain.voiceMail.repository.VoiceMailRepository;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoiceMailService {

    private final VoiceMailRepository voiceMailRepository;

    private final S3Service s3Service;

    public AllVoiceMailsResponse getAllVoiceMails(User user) {
        List<VoiceMail> voiceMails = voiceMailRepository.findAllByFamilyIdOrderByCreatedAtDesc(user.getFamily().getId());
        return new AllVoiceMailsResponse(voiceMails);
    }

    @Transactional
    public void createVoiceMail(VoiceMailRequest voiceMailRequest, User user) {
        UploadResponse uploadResponse = s3Service.uploadFile(voiceMailRequest.getSound());
        voiceMailRepository.save(new VoiceMail(voiceMailRequest, uploadResponse, user));
        user.getFamily().plusScore(20);
    }

    @Transactional
    public void deleteVoiceMail(Long voiceMailId, User user) {
        VoiceMail voiceMail = voiceMailRepository.findById(voiceMailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "음성메시지를 찾을 수 없습니다"));
        if (!voiceMail.getFamily().getId().equals(user.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "음성메시지 삭제 권한이 없습니다");
        }
        voiceMailRepository.deleteById(voiceMailId);
        s3Service.deleteFiles(Collections.singletonList(voiceMail.getSoundFilename()));
        user.getFamily().minusScore(20);
    }
}
