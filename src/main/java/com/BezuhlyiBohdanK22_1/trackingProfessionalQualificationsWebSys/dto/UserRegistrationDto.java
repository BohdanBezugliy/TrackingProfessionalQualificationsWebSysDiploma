package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.enums.UserRole;

/**
 * DTO (Data Transfer Object) для реєстрації нового користувача в системі.
 * Містить облікові дані, необхідні для створення профілю та надання прав доступу.
 *
 * @param email Електронна адреса користувача, що використовується як логін
 * @param password Пароль користувача для входу в систему
 * @param role Роль користувача, яка визначає його права доступу (наприклад, викладач, адміністратор)
 */
public record UserRegistrationDto(
    String email,
    String password,
    UserRole role
){}
