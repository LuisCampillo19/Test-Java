package com.talentboard.service;

import com.talentboard.dto.request.CreateUserRequest;
import com.talentboard.dto.request.RegisterRequest;
import com.talentboard.dto.response.UserResponse;
import com.talentboard.entity.User;
import com.talentboard.enums.Role;
import com.talentboard.exception.BusinessRuleException;
import com.talentboard.mapper.UserMapper;
import com.talentboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /** Public self-registration. Always creates a CANDIDATE. */
    @Transactional
    public UserResponse register(RegisterRequest request) {
        ensureEmailIsFree(request.getEmail());
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CANDIDATE)
                .enabled(true)
                .build();
        return userMapper.toResponse(userRepository.save(user));
    }

    /** ADMIN-only creation, allowing any role. */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        ensureEmailIsFree(request.getEmail());
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    private void ensureEmailIsFree(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email already registered: " + email);
        }
    }
}
