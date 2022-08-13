package com.example.harmony.domain.voiceMail.service;

import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.user.repository.UserRepository;
import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import com.example.harmony.domain.voiceMail.repository.VoiceMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoiceMailService {

    private final VoiceMailRepository voiceMailRepository;
    private final UserRepository userRepository;

    public AllVoiceMailsResponse allVoiceMails(User user){//소리샘 목록

        List<VoiceMail> VoiceMails=voiceMailRepository.FindAllByFamilyIdByCreatedAtDesc(user.getFamily().getId());
        return new AllVoiceMailsResponse((VoiceMail) VoiceMails);
    }
    public ResponseEntity<?> createVoiceMail(VoiceMailRequest voiceMailRequest, User user){

    }

}
