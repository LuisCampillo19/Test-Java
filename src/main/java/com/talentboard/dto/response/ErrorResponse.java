package com.talentboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/** Standard error payload returned by the API. */
@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    /** Field -> validation message, only present for validation errors. */
    private Map<String, String> validationErrors;
}
