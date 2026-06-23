package com.talentboard.service;

import com.talentboard.config.SecurityUtil;
import com.talentboard.dto.request.InterviewRequest;
import com.talentboard.dto.request.InterviewResultUpdateRequest;
import com.talentboard.dto.response.InterviewResponse;
import com.talentboard.entity.Application;
import com.talentboard.entity.Interview;
import com.talentboard.entity.User;
import com.talentboard.enums.ApplicationStatus;
import com.talentboard.enums.InterviewResult;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.exception.ResourceNotFoundException;
import com.talentboard.mapper.InterviewMapper;
import com.talentboard.repository.ApplicationRepository;
import com.talentboard.repository.InterviewRepository;
import com.talentboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final InterviewMapper interviewMapper;
    private final SecurityUtil securityUtil;

    /** Schedules an interview for an application. */
    @Transactional
    public InterviewResponse schedule(InterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found: " + request.getApplicationId()));

        // Rule: interviews cannot be scheduled before the current date.
        if (request.getScheduledDate().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Interviews cannot be scheduled in the past");
        }

        User responsible = resolveResponsible(request);

        Interview interview = Interview.builder()
                .application(application)
                .scheduledDate(request.getScheduledDate())
                .scheduledTime(request.getScheduledTime())
                .type(request.getType())
                .responsible(responsible)
                .result(InterviewResult.PENDING)
                .observations(request.getObservations())
                .build();

        Interview saved = interviewRepository.save(interview);

        // Move the application into the INTERVIEW stage automatically.
        application.setStatus(ApplicationStatus.INTERVIEW);
        applicationRepository.save(application);

        return interviewMapper.toResponse(saved);
    }

    /** Records the outcome of an interview once it has taken place. */
    @Transactional
    public InterviewResponse updateResult(Long id, InterviewResultUpdateRequest request) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found: " + id));
        interview.setResult(request.getResult());
        if (request.getObservations() != null) {
            interview.setObservations(request.getObservations());
        }
        return interviewMapper.toResponse(interviewRepository.save(interview));
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId)
                .stream().map(interviewMapper::toResponse).toList();
    }

    /** If no responsible is supplied, defaults to the current recruiter. */
    private User resolveResponsible(InterviewRequest request) {
        if (request.getResponsibleId() != null) {
            return userRepository.findById(request.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Responsible user not found: " + request.getResponsibleId()));
        }
        return securityUtil.getCurrentUser();
    }
}
