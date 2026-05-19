package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import java.time.LocalDate;

public record EducationDto(
        String institutionName,
        String specialization,
        LocalDate endingDate
) {}
