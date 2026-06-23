package com.talentboard.dto.request;

import com.talentboard.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class InterviewRequest {

    @NotNull(message = "Application id is required")
    private Long applicationId;

    // Explicit ISO formats so binding from the HTML date/time inputs never fails.
    @NotNull(message = "Scheduled date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate scheduledDate;

    @NotNull(message = "Scheduled time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime scheduledTime;

    @NotNull(message = "Interview type is required")
    private InterviewType type;

    /** Optional: defaults to the recruiter who creates the interview. */
    private Long responsibleId;

    private String observations;
}
