package com.talentboard.repository;

import com.talentboard.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationId(Long applicationId);

    List<Interview> findByResponsibleId(Long responsibleId);

    /** Used to prevent double-booking an interviewer on the same date and time. */
    boolean existsByResponsibleIdAndScheduledDateAndScheduledTime(
            Long responsibleId, LocalDate scheduledDate, LocalTime scheduledTime);
}
