package com.example.harmony.domain.schedule.repository;

import com.example.harmony.domain.schedule.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}