package com.talentboard.config;

import com.talentboard.entity.User;
import com.talentboard.exception.AccessDeniedAppException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Helper to obtain the currently authenticated User entity. */
@Component
public class SecurityUtil {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails details)) {
            throw new AccessDeniedAppException("No authenticated user");
        }
        return details.getUser();
    }
}
