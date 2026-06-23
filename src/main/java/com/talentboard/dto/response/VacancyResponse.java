package com.talentboard.dto.response;

import com.talentboard.enums.VacancyStatus;
import com.talentboard.enums.WorkModality;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class VacancyResponse {
    private Long id;
    private String title;
    private String description;
    private String area;
    private WorkModality workModality;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDate publicationDate;
    private VacancyStatus status;
    private Long responsibleUserId;
    private String responsibleUserName;
}
