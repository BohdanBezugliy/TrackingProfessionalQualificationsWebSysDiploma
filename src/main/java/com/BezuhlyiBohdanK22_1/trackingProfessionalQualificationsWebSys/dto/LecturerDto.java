package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDto {
    private Long lectureId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String academicDegree;
    private String academicRank;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    private Long departmentId;
    private String departmentName;
    
    private Long facultyId;
    private String facultyName;
    
    private List<EducationDto> educations;
    
    private Integer totalHours;
    private java.math.BigDecimal totalEctsCredits;
}
