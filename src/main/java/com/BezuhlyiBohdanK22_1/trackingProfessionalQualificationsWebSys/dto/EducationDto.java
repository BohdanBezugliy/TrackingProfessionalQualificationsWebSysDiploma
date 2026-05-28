package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) для передачі даних про освіту викладача.
 * Містить інформацію про навчальний заклад, спеціальність та дату завершення навчання.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {

    /**
     * Унікальний ідентифікатор запису про освіту.
     */
    private Long id;

    /**
     * Назва навчального закладу, який закінчив викладач.
     */
    private String institutionName;

    /**
     * Спеціальність, за якою було здобуто освіту.
     */
    private String specialization;
    
    /**
     * Дата завершення навчання в закладі освіти.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endingDate;
}
