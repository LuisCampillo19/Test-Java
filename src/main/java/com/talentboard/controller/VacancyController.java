package com.talentboard.controller;

import com.talentboard.dto.request.VacancyRequest;
import com.talentboard.dto.request.VacancyStatusUpdateRequest;
import com.talentboard.dto.response.VacancyResponse;
import com.talentboard.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    public ResponseEntity<List<VacancyResponse>> getAll() {
        return ResponseEntity.ok(vacancyService.getAll());
    }

    @GetMapping("/open")
    public ResponseEntity<List<VacancyResponse>> getOpen() {
        return ResponseEntity.ok(vacancyService.getOpen());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<VacancyResponse> create(@Valid @RequestBody VacancyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<VacancyResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody VacancyRequest request) {
        return ResponseEntity.ok(vacancyService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<VacancyResponse> changeStatus(@PathVariable Long id,
                                                        @Valid @RequestBody VacancyStatusUpdateRequest request) {
        return ResponseEntity.ok(vacancyService.changeStatus(id, request.getStatus()));
    }
}
