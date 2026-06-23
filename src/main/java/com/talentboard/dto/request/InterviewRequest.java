package com.talentboard.dto.request;

import com.talentboard.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InterviewRequest {

    @NotNull(message = "Application id is required")
    private Long applicationId;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    @NotNull(message = "Scheduled time is required")
    private LocalTime scheduledTime;

    @NotNull(message = "Interview type is required")
    private InterviewType type;

    /** Optional: defaults to the recruiter who creates the interview. */
    private Long responsibleId;

    private String observations;
}
