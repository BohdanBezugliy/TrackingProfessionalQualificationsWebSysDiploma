package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.enums.UserRole;

public record UserRegistrationDto(
    String email,
    String password,
    UserRole role
){}
