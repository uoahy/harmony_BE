package com.example.harmony.domain.voiceMail.repository;

import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class VoiceMailRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    VoiceMailRepository voiceMailRepository;

    @Test
    @DisplayName("월별 일정 조회")
    void findAllByFamilyIdOrderByCreatedAtDesc() {
        // given
        Family family1 = Family.builder().build();

        VoiceMail voiceMail1 = VoiceMail.builder()
                .family(family1)
                .build();

        VoiceMail voiceMail2 = VoiceMail.builder()
                .family(family1)
                .build();

        VoiceMail voiceMail3 = VoiceMail.builder()
                .family(family1)
                .build();

        Family family2 = Family.builder().build();

        VoiceMail voiceMail4 = VoiceMail.builder()
                .family(family2)
                .build();

        VoiceMail voiceMail5 = VoiceMail.builder()
                .family(family2)
                .build();

        em.persist(family1);
        em.persist(voiceMail1);
        em.persist(voiceMail2);
        em.persist(voiceMail3);
        em.persist(family2);
        em.persist(voiceMail4);
        em.persist(voiceMail5);

        // when
        List<VoiceMail> voiceMails = voiceMailRepository.findAllByFamilyIdOrderByCreatedAtDesc(family1.getId());

        // then
        assertEquals(Arrays.asList(voiceMail3, voiceMail2, voiceMail1), voiceMails);
    }
}