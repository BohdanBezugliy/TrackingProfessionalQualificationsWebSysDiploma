package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Сутність, що представляє обліковий запис користувача системи.
 * Використовується для автентифікації та авторизації.
 * Відображається на таблицю "users" у базі даних.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    /**
     * Унікальний ідентифікатор користувача.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * Електронна адреса користувача, що використовується як логін.
     */
    @NotBlank
    @Email
    @Column(unique = true)
    @Size(max = 255)
    private String email;

    /**
     * Хеш пароля користувача.
     */
    @NotBlank
    @Column(name = "password_hash", columnDefinition = "TEXT")
    private String passwordHash;

    /**
     * Роль користувача в системі (наприклад, адміністратор або викладач).
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
}
