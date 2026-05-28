package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторій для доступу до даних користувачів.
 * Забезпечує виконання базових CRUD операцій та спеціалізованих запитів для сутності {@link UserEntity}.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Знаходить користувача за його електронною адресою.
     *
     * @param email електронна адреса користувача.
     * @return {@link Optional}, що містить сутність користувача, якщо такого знайдено, інакше - порожній.
     */
    Optional<UserEntity> findByEmail(String email);
    /**
     * Перевіряє, чи існує користувач із вказаною електронною адресою.
     *
     * @param email електронна адреса користувача.
     * @return {@code true}, якщо користувач існує, інакше - {@code false}.
     */
    boolean existsByEmail(String email);
}
