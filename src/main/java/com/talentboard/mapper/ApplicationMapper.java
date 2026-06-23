package com.talentboard.mapper;

import com.talentboard.dto.response.ApplicationResponse;
import com.talentboard.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .candidateId(application.getCandidate().getId())
                .candidateName(application.getCandidate().getFullName())
                .vacancyId(application.getVacancy().getId())
                .vacancyTitle(application.getVacancy().getTitle())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .observations(application.getObservations())
                .build();
    }
}
