package com.talentboard.enums;

/**
 * Selection-process flow for a candidate's application.
 *
 * Intended forward path:
 *   APPLIED -> IN_REVIEW -> INTERVIEW -> OFFERED -> HIRED
 * Terminal states (can be reached from several points):
 *   REJECTED, WITHDRAWN
 *
 * Valid transitions are enforced in the service layer, NOT here.
 */
public enum ApplicationStatus {
    APPLIED,     // Candidate just applied
    IN_REVIEW,   // Recruiter is reviewing the profile
    INTERVIEW,   // In the interview stage
    OFFERED,     // Offer extended
    HIRED,       // Process finished successfully
    REJECTED,    // Discarded by the recruiter
    WITHDRAWN    // Candidate withdrew
}
