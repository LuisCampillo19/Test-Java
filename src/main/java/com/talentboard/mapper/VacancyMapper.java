package com.talentboard.mapper;

import com.talentboard.dto.request.VacancyRequest;
import com.talentboard.dto.response.VacancyResponse;
import com.talentboard.entity.Vacancy;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {

    /** Builds a new entity from the request. Relationships/status/date are set in the service. */
    public Vacancy toEntity(VacancyRequest request) {
        return Vacancy.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .area(request.getArea())
                .workModality(request.getWorkModality())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .build();
    }

    /** Applies editable fields from the request onto an existing entity. */
    public void updateEntity(Vacancy vacancy, VacancyRequest request) {
        vacancy.setTitle(request.getTitle());
        vacancy.setDescription(request.getDescription());
        vacancy.setArea(request.getArea());
        vacancy.setWorkModality(request.getWorkModality());
        vacancy.setSalaryMin(request.getSalaryMin());
        vacancy.setSalaryMax(request.getSalaryMax());
    }

    public VacancyResponse toResponse(Vacancy vacancy) {
        return VacancyResponse.builder()
                .id(vacancy.getId())
                .title(vacancy.getTitle())
                .description(vacancy.getDescription())
                .area(vacancy.getArea())
                .workModality(vacancy.getWorkModality())
                .salaryMin(vacancy.getSalaryMin())
                .salaryMax(vacancy.getSalaryMax())
                .publicationDate(vacancy.getPublicationDate())
                .status(vacancy.getStatus())
                .responsibleUserId(vacancy.getResponsibleUser().getId())
                .responsibleUserName(vacancy.getResponsibleUser().getFullName())
                .build();
    }
}
