package com.talentboard.dto.response;

import com.talentboard.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long vacancyId;
    private String vacancyTitle;
    private LocalDateTime applicationDate;
    private ApplicationStatus status;
    private String observations;
}
