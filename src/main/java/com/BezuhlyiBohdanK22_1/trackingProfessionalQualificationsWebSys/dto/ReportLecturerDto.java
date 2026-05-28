package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) для генерації та відображення звітів по викладачах.
 * Об'єднує особисту інформацію, дані про освіту, наукові ступені та активності з підвищення кваліфікації
 * у зручному для звітності текстовому форматі.
 */
@Data
public class ReportLecturerDto {

    /**
     * Унікальний ідентифікатор викладача.
     */
    private Long id;

    /**
     * Повне ім'я викладача (ПІБ).
     */
    private String fullName;

    /**
     * Посада, яку займає викладач.
     */
    private String position;

    /**
     * Назва кафедри, за якою закріплено викладача.
     */
    private String departmentName;

    /**
     * Назва факультету, до якого належить викладач.
     */
    private String facultyName;

    /**
     * Детальна інформація про отриману освіту у текстовому форматі.
     */
    private String educationDetails;

    /**
     * Академічна інформація (науковий ступінь та вчене звання).
     */
    private String academicInfo; // degree, rank

    /**
     * Інформація про дисципліни, які викладає особа.
     */
    private String disciplineDetails; // details about discipline

    /**
     * Детальна інформація про пройдені заходи підвищення кваліфікації (відформатований рядок).
     */
    private String upskillingDetails; // formatted string of events
}
