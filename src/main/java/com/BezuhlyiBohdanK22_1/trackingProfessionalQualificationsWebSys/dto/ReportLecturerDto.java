package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.Data;

@Data
public class ReportLecturerDto {
    private Long id;
    private String fullName;
    private String position;
    private String departmentName;
    private String facultyName;
    private String educationDetails;
    private String academicInfo; // degree, rank
    private String upskillingDetails; // formatted string of events
}
