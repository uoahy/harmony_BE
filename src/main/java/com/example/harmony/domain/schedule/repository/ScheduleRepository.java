package com.example.harmony.domain.schedule.repository;

import com.example.harmony.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByFamilyIdAndStartDateBeforeAndEndDateAfter(Long familyId, LocalDate startDate, LocalDate endDate);
}