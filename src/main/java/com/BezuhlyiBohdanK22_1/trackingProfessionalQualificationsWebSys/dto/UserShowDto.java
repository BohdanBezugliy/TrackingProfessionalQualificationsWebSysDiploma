package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

/**
 * DTO (Data Transfer Object) для відображення базової інформації про користувача.
 * Використовується для передачі мінімально необхідних даних (наприклад, у списках або коротких профілях).
 *
 * @param email Електронна адреса користувача
 */
public record UserShowDto (
        String email
){}
