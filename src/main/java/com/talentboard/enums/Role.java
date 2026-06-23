package com.talentboard.enums;

/**
 * System roles. Each user has exactly one role that determines their permissions.
 */
public enum Role {
    ADMIN,      // Full access: manages users and the whole system
    RECRUITER,  // Manages vacancies, reviews applications, schedules interviews
    CANDIDATE   // Applies to vacancies and tracks only their own applications
}
