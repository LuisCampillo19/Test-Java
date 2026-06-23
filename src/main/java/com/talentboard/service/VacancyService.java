package com.talentboard.service;

import com.talentboard.config.SecurityUtil;
import com.talentboard.dto.request.VacancyRequest;
import com.talentboard.dto.response.VacancyResponse;
import com.talentboard.entity.User;
import com.talentboard.entity.Vacancy;
import com.talentboard.enums.VacancyStatus;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.exception.ResourceNotFoundException;
import com.talentboard.mapper.VacancyMapper;
import com.talentboard.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final VacancyMapper vacancyMapper;
    private final SecurityUtil securityUtil;

    @Transactional
    public VacancyResponse create(VacancyRequest request) {
        validateSalaryRange(request);
        User current = securityUtil.getCurrentUser();

        Vacancy vacancy = vacancyMapper.toEntity(request);
        vacancy.setResponsibleUser(current);
        vacancy.setStatus(VacancyStatus.OPEN);
        vacancy.setPublicationDate(LocalDate.now());

        return vacancyMapper.toResponse(vacancyRepository.save(vacancy));
    }

    @Transactional
    public VacancyResponse update(Long id, VacancyRequest request) {
        validateSalaryRange(request);
        Vacancy vacancy = findEntity(id);
        vacancyMapper.updateEntity(vacancy, request);
        return vacancyMapper.toResponse(vacancyRepository.save(vacancy));
    }

    @Transactional
    public VacancyResponse changeStatus(Long id, VacancyStatus status) {
        Vacancy vacancy = findEntity(id);
        vacancy.setStatus(status);
        return vacancyMapper.toResponse(vacancyRepository.save(vacancy));
    }

    @Transactional(readOnly = true)
    public VacancyResponse getById(Long id) {
        return vacancyMapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<VacancyResponse> getAll() {
        return vacancyRepository.findAll().stream().map(vacancyMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<VacancyResponse> getOpen() {
        return vacancyRepository.findByStatus(VacancyStatus.OPEN)
                .stream().map(vacancyMapper::toResponse).toList();
    }

    /** Returns the raw entity. Used by other services that need the managed object. */
    @Transactional(readOnly = true)
    public Vacancy findEntity(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy not found: " + id));
    }

    private void validateSalaryRange(VacancyRequest request) {
        if (request.getSalaryMin() != null && request.getSalaryMax() != null
                && request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {
            throw new BusinessRuleException("Minimum salary cannot be greater than maximum salary");
        }
    }
}
