package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.initializer;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserShowDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.enums.UserRole;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.UserNotFoundException;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Компонент, який виконується під час запуску програми та відповідає за ініціалізацію
 * облікового запису адміністратора за замовчуванням.
 * Реалізує інтерфейс {@link CommandLineRunner}.
 */
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    /**
     * Енкодер для хешування паролів.
     */
    private final PasswordEncoder encoder;
    /**
     * Сервіс для управління даними користувачів.
     */
    private final UserService userService;

    /**
     * Електронна пошта адміністратора, зчитана з конфігураційних файлів (наприклад, application.properties).
     */
    @Value("${app.admin.email}")
    private String adminEmail;

    /**
     * Пароль адміністратора, зчитаний з конфігураційних файлів.
     */
    @Value("${app.admin.password}")
    private String adminPassword;

    /**
     * Метод, що викликається після ініціалізації контексту Spring.
     * Перевіряє, чи існує користувач з email адміністратора. Якщо не існує — створює його
     * з роллю ADMIN.
     *
     * @param args аргументи командного рядка
     */
    @Override
    public void run(String ... args) {
        try {
            UserShowDto user = userService.getUserByEmail(adminEmail);
        }catch (UserNotFoundException e){
            UserRegistrationDto admin = new UserRegistrationDto(adminEmail, adminPassword, UserRole.ADMIN);
            userService.save(admin);
        }
    }
}
