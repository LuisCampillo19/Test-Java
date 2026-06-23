package com.talentboard.repository;

import com.talentboard.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /** Used to enforce "a candidate cannot apply twice to the same vacancy". */
    boolean existsByCandidateIdAndVacancyId(Long candidateId, Long vacancyId);

    /** Candidates only see their own applications. */
    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByVacancyId(Long vacancyId);
}
