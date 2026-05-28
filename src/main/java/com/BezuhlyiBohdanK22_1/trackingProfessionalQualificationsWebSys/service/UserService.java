package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UserShowDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.enums.UserRole;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.UserNotFoundException;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Реалізація сервісу для управління користувачами системи.
 * Відповідає за реєстрацію нових користувачів, шифрування їх паролів 
 * та отримання інформації про наявних користувачів.
 */
@Service
public class UserService implements IUserService {
    
    /**
     * Репозиторій для роботи з сутностями користувачів.
     */
    private final UserRepository userRepository;
    
    /**
     * Компонент для шифрування паролів.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Перетворює DTO з даними для реєстрації користувача у відповідну сутність.
     * Забезпечує шифрування пароля та встановлення ролі користувача.
     * 
     * @param userRegistrationDto об'єкт {@link UserRegistrationDto} з даними для реєстрації
     * @return об'єкт сутності {@link UserEntity}, готовий для збереження в базі даних
     */
    private UserEntity UserRegistrationDtoToEntity(UserRegistrationDto userRegistrationDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.USER);
        userEntity.setEmail(userRegistrationDto.email());
        userEntity.setPasswordHash(passwordEncoder.encode(userRegistrationDto.password()));
        userEntity.setUserRole(userRegistrationDto.role());
        return userEntity;
    }

    /**
     * Конструктор для ініціалізації залежностей сервісу.
     * 
     * @param userRepository репозиторій для роботи з користувачами
     * @param passwordEncoder компонент для шифрування паролів
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Зберігає нового користувача в базі даних на основі переданих реєстраційних даних.
     * 
     * @param user об'єкт {@link UserRegistrationDto} з даними для реєстрації нового користувача
     */
    @Override
    public void save(UserRegistrationDto user) {
        UserEntity userEntity = UserRegistrationDtoToEntity(user);
        userRepository.save(userEntity);
    }

    /**
     * Отримує інформацію про користувача за його електронною поштою.
     * 
     * @param email електронна пошта користувача, якого потрібно знайти
     * @return об'єкт {@link UserShowDto} з відомостями про знайденого користувача
     * @throws UserNotFoundException якщо користувача з вказаним email не знайдено
     */
    @Override
    public UserShowDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> new UserShowDto(userEntity.getEmail()))
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
