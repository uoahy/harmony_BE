package com.example.harmony.domain.user.repository;

import com.example.harmony.domain.user.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
