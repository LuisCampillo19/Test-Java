package com.talentboard.dto.request;

import com.talentboard.enums.VacancyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VacancyStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private VacancyStatus status;
}
