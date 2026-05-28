package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception;

/**
 * Виняток, який викидається, коли лекцію не знайдено в системі.
 * Успадковується від {@link RuntimeException}.
 */
public class LectureNotFoundException extends RuntimeException {
    /**
     * Конструктор для створення винятку з повідомленням.
     *
     * @param message повідомлення з деталями про помилку
     */
    public LectureNotFoundException(String message) {
        super(message);
    }
}
