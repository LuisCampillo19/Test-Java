package com.talentboard.dto.response;

import com.talentboard.enums.InterviewResult;
import com.talentboard.enums.InterviewType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class InterviewResponse {
    private Long id;
    private Long applicationId;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private InterviewType type;
    private Long responsibleId;
    private String responsibleName;
    private InterviewResult result;
    private String observations;
}
