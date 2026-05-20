package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import java.time.LocalDate;
import java.util.List;

public record LecturerUpdateDto(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String academicDegree,
        String academicRank,
        LocalDate hireDate,
        Long departmentId,
        List<EducationDto> educations
) {}
