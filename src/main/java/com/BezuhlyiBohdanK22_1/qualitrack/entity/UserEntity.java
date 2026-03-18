package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import com.BezuhlyiBohdanK22_1.qualitrack.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @NotBlank
    @Email
    @Column(unique = true)
    @Size(max = 255)
    private String email;

    @NotBlank
    @Column(name = "password_hash", columnDefinition = "TEXT")
    private String passwordHash;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
}
