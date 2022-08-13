package com.example.harmony.domain.voiceMail.repository;

import com.example.harmony.domain.voiceMail.entity.VoiceMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface VoiceMailRepository extends JpaRepository<VoiceMail, Long> {
    List<VoiceMail> FindAllByFamilyIdByCreatedAtDesc(Long familyId);
}