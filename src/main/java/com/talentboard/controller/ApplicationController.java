package com.talentboard.controller;

import com.talentboard.dto.request.ApplicationRequest;
import com.talentboard.dto.request.ApplicationStatusUpdateRequest;
import com.talentboard.dto.response.ApplicationResponse;
import com.talentboard.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> apply(@Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>> myApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    /** Ownership is enforced inside the service for candidates. */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @GetMapping("/vacancy/{vacancyId}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<List<ApplicationResponse>> byVacancy(@PathVariable Long vacancyId) {
        return ResponseEntity.ok(applicationService.getByVacancy(vacancyId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long id,
                                                            @Valid @RequestBody ApplicationStatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateStatus(id, request));
    }
}
