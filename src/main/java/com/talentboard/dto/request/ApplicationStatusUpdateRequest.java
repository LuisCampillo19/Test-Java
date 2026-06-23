package com.talentboard.dto.request;

import com.talentboard.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String observations;
}
