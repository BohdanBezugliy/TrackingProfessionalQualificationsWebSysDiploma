package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO (Data Transfer Object) для передачі інформації про факультет.
 * Використовується для інкапсуляції даних факультету при обміні між клієнтом та сервером.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDto {

    /**
     * Унікальний ідентифікатор факультету.
     */
    private Long facultyId;

    /**
     * Назва факультету.
     */
    private String facultyName;

    /**
     * Список кафедр, які належать до даного факультету.
     */
    private List<DepartmentDto> departments;
}
