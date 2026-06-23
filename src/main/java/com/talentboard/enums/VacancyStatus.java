package com.talentboard.enums;

/**
 * Lifecycle of a vacancy.
 * Business rule: applications are only accepted when status == OPEN.
 */
public enum VacancyStatus {
    DRAFT,   // Being prepared, not visible to candidates yet
    OPEN,    // Published and accepting applications
    CLOSED   // No longer accepting applications
}
