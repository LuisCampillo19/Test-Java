package com.talentboard.config;

import com.talentboard.entity.User;
import com.talentboard.enums.Role;
import com.talentboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds one user per role on first startup, so the app is usable immediately.
 * Only runs if the users table is empty.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        createUser("Admin User", "admin@talentboard.com", "admin123", Role.ADMIN);
        createUser("Recruiter User", "recruiter@talentboard.com", "recruiter123", Role.RECRUITER);
        createUser("Candidate User", "candidate@talentboard.com", "candidate123", Role.CANDIDATE);
    }

    private void createUser(String fullName, String email, String rawPassword, Role role) {
        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .enabled(true)
                .build();
        userRepository.save(user);
    }
}
