package com.talentboard.dto.request;

import com.talentboard.enums.WorkModality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VacancyRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Area is required")
    private String area;

    @NotNull(message = "Work modality is required")
    private WorkModality workModality;

    @PositiveOrZero(message = "Minimum salary cannot be negative")
    private BigDecimal salaryMin;

    @PositiveOrZero(message = "Maximum salary cannot be negative")
    private BigDecimal salaryMax;
}
