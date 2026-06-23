package com.talentboard.service;

import com.talentboard.config.SecurityUtil;
import com.talentboard.dto.request.ApplicationRequest;
import com.talentboard.dto.request.ApplicationStatusUpdateRequest;
import com.talentboard.dto.response.ApplicationResponse;
import com.talentboard.entity.Application;
import com.talentboard.entity.User;
import com.talentboard.entity.Vacancy;
import com.talentboard.enums.ApplicationStatus;
import com.talentboard.enums.Role;
import com.talentboard.enums.VacancyStatus;
import com.talentboard.exception.AccessDeniedAppException;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.exception.ResourceNotFoundException;
import com.talentboard.mapper.ApplicationMapper;
import com.talentboard.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final VacancyService vacancyService;
    private final SecurityUtil securityUtil;

    /** A candidate applies to a vacancy. Enforces the core business rules. */
    @Transactional
    public ApplicationResponse apply(ApplicationRequest request) {
        User candidate = securityUtil.getCurrentUser();
        Vacancy vacancy = vacancyService.findEntity(request.getVacancyId());

        // Rule: only OPEN vacancies accept applications.
        if (vacancy.getStatus() != VacancyStatus.OPEN) {
            throw new BusinessRuleException("Cannot apply to a vacancy that is not open");
        }
        // Rule: a candidate cannot apply twice to the same vacancy.
        if (applicationRepository.existsByCandidateIdAndVacancyId(candidate.getId(), vacancy.getId())) {
            throw new BusinessRuleException("You have already applied to this vacancy");
        }

        Application application = Application.builder()
                .candidate(candidate)
                .vacancy(vacancy)
                .status(ApplicationStatus.APPLIED)
                .observations(request.getObservations())
                .build();

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    /** Candidates only see their own applications. */
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {
        User candidate = securityUtil.getCurrentUser();
        return applicationRepository.findByCandidateId(candidate.getId())
                .stream().map(applicationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getById(Long id) {
        Application application = findEntity(id);
        User current = securityUtil.getCurrentUser();

        // Rule: a candidate may only access their own applications.
        if (current.getRole() == Role.CANDIDATE
                && !application.getCandidate().getId().equals(current.getId())) {
            throw new AccessDeniedAppException("You can only view your own applications");
        }
        return applicationMapper.toResponse(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getByVacancy(Long vacancyId) {
        return applicationRepository.findByVacancyId(vacancyId)
                .stream().map(applicationMapper::toResponse).toList();
    }

    /** Recruiter/Admin advances the application through the selection flow. */
    @Transactional
    public ApplicationResponse updateStatus(Long id, ApplicationStatusUpdateRequest request) {
        Application application = findEntity(id);
        application.setStatus(request.getStatus());
        if (request.getObservations() != null) {
            application.setObservations(request.getObservations());
        }
        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public Application findEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
    }
}
