package com.example.harmony.domain.voiceMail.service;

import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.domain.voiceMail.dto.AllVoiceMailsResponse;
import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import com.example.harmony.domain.voiceMail.repository.VoiceMailRepository;
import com.example.harmony.global.s3.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoiceMailServiceTest {

    @InjectMocks
    VoiceMailService voiceMailService;

    @Mock
    VoiceMailRepository voiceMailRepository;

    @Mock
    S3Service s3Service;

    @Nested
    @DisplayName("소리샘 조회")
    class GetAllVoiceMails {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                Long familyId = 1L;

                Family family = Family.builder()
                        .id(familyId)
                        .build();

                User user = User.builder()
                        .family(family)
                        .build();

                VoiceMail voiceMail1 = VoiceMail.builder().build();

                VoiceMail voiceMail2 = VoiceMail.builder().build();

                List<VoiceMail> voiceMails = Arrays.asList(voiceMail1, voiceMail2);

                when(voiceMailRepository.findAllByFamilyIdOrderByCreatedAtDesc(familyId))
                        .thenReturn(voiceMails);

                // when
                AllVoiceMailsResponse allVoiceMailsResponse = voiceMailService.getAllVoiceMails(user);

                // then
                assertEquals(2, allVoiceMailsResponse.getVoiceMails().size());
            }
        }
    }
}