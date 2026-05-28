package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачі даних про навчальну дисципліну.
 * Використовується для відображення дисциплін та кількості пов'язаних з ними заходів підвищення кваліфікації.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineDto {

    /**
     * Унікальний ідентифікатор навчальної дисципліни.
     */
    private Long disciplineId;

    /**
     * Назва навчальної дисципліни.
     */
    private String disciplineName;

    /**
     * Кількість заходів підвищення кваліфікації (документів), що пов'язані з цією дисципліною.
     * Використовується для відображення статистики (наприклад, "X пов'язаних документів").
     */
    private int upskillEventsCount;
}
