package com.example.harmony.domain.user.repository;

import com.example.harmony.domain.user.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {
    Optional<Family> findByFamilyCode(String familyCode);

    Optional<Family> findByFamilyName(String familyName);
    Optional<Family> findById(Long id);

    List<Family> findTop10ByOrderByWeeklyScoreDesc();

    List<Family> findAllByOrderByWeeklyScoreDesc();

    List<Family> findAllByFlower(boolean flower);
}