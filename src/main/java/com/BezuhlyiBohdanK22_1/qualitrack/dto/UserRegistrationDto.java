package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import com.BezuhlyiBohdanK22_1.qualitrack.enums.UserRole;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
    String email,
    @Size(min = 6, max = 20)
    String password,
    UserRole role
){}
