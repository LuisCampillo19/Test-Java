package com.talentboard.web;

import com.talentboard.dto.request.ApplicationStatusUpdateRequest;
import com.talentboard.dto.request.InterviewRequest;
import com.talentboard.dto.request.InterviewResultUpdateRequest;
import com.talentboard.dto.response.ApplicationResponse;
import com.talentboard.dto.response.InterviewResponse;
import com.talentboard.enums.ApplicationStatus;
import com.talentboard.enums.InterviewResult;
import com.talentboard.enums.InterviewType;
import com.talentboard.enums.Role;
import com.talentboard.repository.UserRepository;
import com.talentboard.service.ApplicationService;
import com.talentboard.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thymeleaf views for applications and their interview agenda.
 *
 * - Candidates browse their own applications and see their scheduled interviews.
 * - Recruiters/Admins open an application detail where they advance the
 *   selection status and schedule / record interviews.
 */
@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationViewController {

    private final ApplicationService applicationService;
    private final InterviewService interviewService;
    private final UserRepository userRepository;

    /** Candidate view: own applications, each with its interview agenda. */
    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public String myApplications(Model model) {
        List<ApplicationResponse> applications = applicationService.getMyApplications();
        Map<Long, List<InterviewResponse>> interviewsByApplication = new HashMap<>();
        for (ApplicationResponse application : applications) {
            interviewsByApplication.put(
                    application.getId(),
                    interviewService.getByApplication(application.getId()));
        }
        model.addAttribute("applications", applications);
        model.addAttribute("interviewsByApplication", interviewsByApplication);
        return "my-applications";
    }

    /** Recruiter/Admin view: application detail + interview agenda management. */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public String detail(@PathVariable Long id, Model model) {
        // NOTE: the model attribute must NOT be named "application": Thymeleaf
        // reserves "application" as an implicit web-scope object (ServletContext
        // attributes), which shadows the model variable and makes every
        // ${application.*} expression resolve to empty.
        model.addAttribute("app", applicationService.getById(id));
        model.addAttribute("interviews", interviewService.getByApplication(id));
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("interviewTypes", InterviewType.values());
        model.addAttribute("interviewResults", InterviewResult.values());
        model.addAttribute("responsibles", userRepository.findByRole(Role.RECRUITER));
        // Used to block past dates in the interview date picker (client-side guard
        // that mirrors the server-side "no interviews in the past" rule).
        model.addAttribute("today", LocalDate.now());
        return "application-detail";
    }

    /** Recruiter/Admin: advance the application through the selection flow. */
    @PostMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public String updateStatus(@PathVariable Long id,
                               @ModelAttribute ApplicationStatusUpdateRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateStatus(id, request);
            redirectAttributes.addFlashAttribute("success", "Application status updated");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/applications/" + id;
    }

    /** Recruiter/Admin: schedule (agendar) an interview for this application. */
    @PostMapping("/{id}/interviews")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public String scheduleInterview(@PathVariable Long id,
                                    @ModelAttribute InterviewRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            request.setApplicationId(id);
            interviewService.schedule(request);
            redirectAttributes.addFlashAttribute("success", "Interview scheduled");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/applications/" + id;
    }

    /** Recruiter/Admin: record the outcome of an interview. */
    @PostMapping("/{id}/interviews/{interviewId}/result")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public String recordResult(@PathVariable Long id,
                               @PathVariable Long interviewId,
                               @ModelAttribute InterviewResultUpdateRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            interviewService.updateResult(interviewId, request);
            redirectAttributes.addFlashAttribute("success", "Interview result recorded");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/applications/" + id;
    }
}
