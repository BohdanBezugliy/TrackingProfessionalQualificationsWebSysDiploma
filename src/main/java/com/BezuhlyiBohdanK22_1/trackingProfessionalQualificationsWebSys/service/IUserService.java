package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserShowDto;

/**
 * Інтерфейс сервісу для управління користувачами системи.
 * Визначає контракти для збереження та отримання інформації про користувачів.
 */
public interface IUserService {
    
    /**
     * Зберігає нового користувача в базі даних на основі переданих реєстраційних даних.
     * 
     * @param user об'єкт {@link UserRegistrationDto} з даними для реєстрації нового користувача
     */
    void save(UserRegistrationDto user);

    /**
     * Отримує інформацію про користувача за його електронною поштою.
     * 
     * @param email електронна пошта користувача, якого потрібно знайти
     * @return об'єкт {@link UserShowDto} з відомостями про знайденого користувача
     * @throws com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.UserNotFoundException якщо користувача з вказаним email не знайдено
     */
    UserShowDto getUserByEmail(String email);
}
