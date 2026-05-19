package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import java.time.LocalDate;
import java.util.List;

public record LectureDto(
        UserRegistrationDto userRegistrationDto,

        String firstName,
        String middleName,
        String lastName,
        String academicDegree,
        String academicRank,
        LocalDate hireDate,

        Long departmentId,
        Long facultyId,
        List<EducationDto> educations
) {}
