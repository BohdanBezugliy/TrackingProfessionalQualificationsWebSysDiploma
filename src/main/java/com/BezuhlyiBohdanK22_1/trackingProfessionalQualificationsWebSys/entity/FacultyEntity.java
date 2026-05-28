package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Сутність, що представляє факультет у системі.
 * Відображається на таблицю "faculties" у базі даних.
 */
@Getter
@Setter
@Entity
@Table(name = "faculties")
public class FacultyEntity {
    /**
     * Унікальний ідентифікатор факультету.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long facultyId;

    /**
     * Назва факультету.
     */
    @NotBlank(message = "Назва факультету обов'язкова")
    @Column(name = "faculty_name")
    @Size(max = 255)
    private String facultyName;

    /**
     * Список кафедр, що належать до цього факультету.
     * Зв'язок один-до-багатьох із {@link DepartmentEntity}.
     */
    @OneToMany(mappedBy = "facultyEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DepartmentEntity> departments;
}