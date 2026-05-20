package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import com.BezuhlyiBohdanK22_1.qualitrack.enums.UserRole;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
    String email,
    String password,
    UserRole role
){}
