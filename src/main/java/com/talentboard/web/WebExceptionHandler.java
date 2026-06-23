package com.talentboard.web;

import com.talentboard.exception.AccessDeniedAppException;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.exception.ResourceNotFoundException;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import java.math.BigDecimal;

/**
 * Renders the friendly error page (templates/error.html) for any exception
 * thrown by the Thymeleaf view controllers, so users never hit the raw Spring
 * "Whitelabel" error page.
 *
 * Business/validation errors raised inside a form action are already shown
 * inline by each controller (flash messages); this is the safety net for
 * everything else — most notably request-binding failures (HTTP 400), which
 * happen before the controller method runs and therefore cannot be caught there.
 */
@ControllerAdvice(basePackages = "com.talentboard.web")
public class WebExceptionHandler {

    /**
     * Normalizes empty form fields to {@code null} so that an unselected optional
     * dropdown/number (e.g. "— Me —" for the interview responsible, or an empty
     * optional salary) does not fail when binding an empty string to a Long /
     * BigDecimal — which otherwise produces an HTTP 400.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        return render(model, 404, "Not found", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedAppException.class)
    public String handleAccessDenied(AccessDeniedAppException ex, Model model) {
        return render(model, 403, "Access denied", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public String handleBusinessRule(BusinessRuleException ex, Model model) {
        return render(model, 409, "Action not allowed", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        return render(model, 400, "Request could not be processed",
                "The submitted data is invalid. Please check the form and try again.");
    }

    private String render(Model model, int status, String error, String message) {
        model.addAttribute("status", status);
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        return "error";
    }
}
