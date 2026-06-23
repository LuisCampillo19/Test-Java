package com.talentboard.repository;

import com.talentboard.entity.Vacancy;
import com.talentboard.enums.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findByStatus(VacancyStatus status);

    List<Vacancy> findByResponsibleUserId(Long responsibleUserId);
}
