package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) для передачі інформації про захід підвищення кваліфікації.
 * Містить деталі про отриманий документ, установу, тему, а також обсяг у годинах та кредитах ЄКТС.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpskillEventDto {

    /**
     * Унікальний ідентифікатор заходу підвищення кваліфікації.
     */
    private Long id;

    /**
     * Тип документа про підвищення кваліфікації (наприклад, сертифікат, диплом).
     */
    private String documentType;

    /**
     * Номер документа, що підтверджує підвищення кваліфікації.
     */
    private String documentNumber;

    /**
     * Назва установи, де відбувалося підвищення кваліфікації.
     */
    private String institutionName;

    /**
     * Тема заходу або курсу підвищення кваліфікації.
     */
    private String topic;

    /**
     * Кількість отриманих кредитів ЄКТС.
     */
    private BigDecimal ectsCredits;

    /**
     * Обсяг підвищення кваліфікації в академічних годинах.
     */
    private Integer hours;

    /**
     * Дата початку проходження заходу підвищення кваліфікації.
     */
    private LocalDate dateBegin;

    /**
     * Дата закінчення проходження заходу підвищення кваліфікації.
     */
    private LocalDate dateEnd;

    /**
     * Дата отримання документа про підвищення кваліфікації.
     */
    private LocalDate dateReceived;

    /**
     * Унікальний ідентифікатор завантаженого файлу (електронної копії документа) у базі даних.
     */
    private Long documentId;

    /**
     * Оригінальна назва завантаженого файлу (електронної копії документа).
     */
    private String originalFileName;

    /**
     * Список ідентифікаторів навчальних дисциплін, які пов'язані з цим заходом підвищення кваліфікації.
     */
    private List<Long> disciplineIds; // IDs of related disciplines
}
