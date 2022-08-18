package com.example.harmony.domain.voiceMail.repository;

import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceMailRepository extends JpaRepository<VoiceMail, Long> {

    List<VoiceMail> findAllByFamilyIdOrderByCreatedAtDesc(Long familyId);
}