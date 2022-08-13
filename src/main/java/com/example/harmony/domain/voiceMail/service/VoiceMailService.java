package com.example.harmony.domain.voiceMail.service;

import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import com.example.harmony.domain.user.repository.UserRepository;
import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.dto.VoiceMailRequest;
import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import com.example.harmony.domain.voiceMail.repository.VoiceMailRepository;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VoiceMailService {

    private final VoiceMailRepository voiceMailRepository;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final S3Service s3Service;

    public AllVoiceMailsResponse allVoiceMails(User user){//소리샘 목록

        List<VoiceMail> VoiceMails=voiceMailRepository.FindAllByFamilyIdByCreatedAtDesc(user.getFamily().getId());
        return new AllVoiceMailsResponse((VoiceMail) VoiceMails);
    }
    @Transactional
    public void createVoiceMail(VoiceMailRequest voiceMailRequest, MultipartFile soundFile, User user){

        User userId = userRepository.findById(user.getId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"등록되지 않은 사용자입니다."));
        Family familyId= familyRepository.findById(user.getFamily().getId())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"등록되지 않은 가족입니다."));
        UploadResponse sound= s3Service.uploadFile(soundFile);

        VoiceMail voiceMail=voiceMailRepository.save(new VoiceMail(userId, familyId, voiceMailRequest, sound));
        //확실치 않음
    }

    @Transactional
    public void deleteVoiceMail()


}
