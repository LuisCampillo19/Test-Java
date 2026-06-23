package com.talentboard.controller;

import com.talentboard.dto.request.InterviewRequest;
import com.talentboard.dto.request.InterviewResultUpdateRequest;
import com.talentboard.dto.response.InterviewResponse;
import com.talentboard.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<InterviewResponse> schedule(@Valid @RequestBody InterviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewService.schedule(request));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponse>> byApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getByApplication(applicationId));
    }

    @PatchMapping("/{id}/result")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<InterviewResponse> updateResult(@PathVariable Long id,
                                                         @Valid @RequestBody InterviewResultUpdateRequest request) {
        return ResponseEntity.ok(interviewService.updateResult(id, request));
    }
}
