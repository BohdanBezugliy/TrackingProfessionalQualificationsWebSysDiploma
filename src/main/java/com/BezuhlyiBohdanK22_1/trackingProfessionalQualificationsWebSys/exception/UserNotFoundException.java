package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception;

/**
 * Виняток, який викидається, коли користувача не знайдено в системі.
 * Успадковується від {@link RuntimeException}.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Конструктор для створення винятку з повідомленням.
     *
     * @param message повідомлення з деталями про помилку
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
