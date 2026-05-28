package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток, який викидається, коли відбувається спроба створення або реєстрації користувача
 * з електронною поштою, яка вже існує в системі.
 * Повертає HTTP-статус 400 (BAD REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Конструктор для створення винятку з повідомленням.
     *
     * @param message повідомлення з деталями про помилку
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
