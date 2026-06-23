package com.talentboard.dto.response;

import com.talentboard.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
