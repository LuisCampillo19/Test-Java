package com.talentboard.dto.request;

import com.talentboard.enums.InterviewResult;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewResultUpdateRequest {

    @NotNull(message = "Result is required")
    private InterviewResult result;

    private String observations;
}
