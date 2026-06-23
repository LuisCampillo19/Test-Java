package com.talentboard.web;

import com.talentboard.config.SecurityUtil;
import com.talentboard.dto.request.ApplicationRequest;
import com.talentboard.dto.request.VacancyRequest;
import com.talentboard.entity.User;
import com.talentboard.enums.Role;
import com.talentboard.enums.WorkModality;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.service.ApplicationService;
import com.talentboard.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyViewController {

    private final VacancyService vacancyService;
    private final ApplicationService applicationService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("vacancies", vacancyService.getAll());
        return "vacancies";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("vacancy", vacancyService.getById(id));
        User current = securityUtil.getCurrentUser();
        model.addAttribute("isCandidate", current.getRole() == Role.CANDIDATE);
        // Recruiters/Admins can see the applications for this vacancy.
        if (current.getRole() != Role.CANDIDATE) {
            model.addAttribute("applications", applicationService.getByVacancy(id));
        }
        return "vacancy-detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("vacancyRequest", new VacancyRequest());
        model.addAttribute("modalities", WorkModality.values());
        return "vacancy-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("vacancyRequest") VacancyRequest request,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("modalities", WorkModality.values());
            return "vacancy-form";
        }
        try {
            vacancyService.create(request);
        } catch (BusinessRuleException ex) {
            model.addAttribute("modalities", WorkModality.values());
            model.addAttribute("error", ex.getMessage());
            return "vacancy-form";
        }
        redirectAttributes.addFlashAttribute("success", "Vacancy created successfully");
        return "redirect:/vacancies";
    }

    @PostMapping("/{id}/apply")
    public String apply(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ApplicationRequest request = new ApplicationRequest();
            request.setVacancyId(id);
            applicationService.apply(request);
            redirectAttributes.addFlashAttribute("success", "Application submitted");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/vacancies/" + id;
    }
}
