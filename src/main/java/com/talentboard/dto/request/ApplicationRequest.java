package com.talentboard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequest {

    @NotNull(message = "Vacancy id is required")
    private Long vacancyId;

    private String observations;
}
