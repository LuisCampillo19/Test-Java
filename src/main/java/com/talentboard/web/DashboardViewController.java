package com.talentboard.web;

import com.talentboard.config.SecurityUtil;
import com.talentboard.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardViewController {

    private final SecurityUtil securityUtil;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User current = securityUtil.getCurrentUser();
        model.addAttribute("user", current);
        return "dashboard";
    }
}
