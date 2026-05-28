package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачі інформації про кафедру.
 * Використовується для обміну даними про підрозділи університету між шарами програми.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    /**
     * Унікальний ідентифікатор кафедри.
     */
    private Long departmentId;

    /**
     * Назва кафедри.
     */
    private String departmentName;

    /**
     * Ідентифікатор факультету, до якого належить ця кафедра.
     */
    private Long facultyId;

    /**
     * Назва факультету, до якого належить ця кафедра.
     */
    private String facultyName;
}
