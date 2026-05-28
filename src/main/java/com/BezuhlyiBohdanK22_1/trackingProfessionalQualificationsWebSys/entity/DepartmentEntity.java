package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Сутність, що представляє кафедру у навчальному закладі.
 * Відображається на таблицю "departments" у базі даних.
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
public class DepartmentEntity {
    /**
     * Унікальний ідентифікатор кафедри.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * Назва кафедри.
     */
    @NotBlank()
    @Column(name = "department_name")
    @Size(max = 255)
    private String departmentName;

    /**
     * Факультет, до якого належить ця кафедра.
     * Зв'язок багато-до-одного із {@link FacultyEntity}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private FacultyEntity facultyEntity;

    /**
     * Список викладачів, які працюють на цій кафедрі.
     * Зв'язок один-до-багатьох із {@link LectureEntity}.
     */
    @OneToMany(mappedBy = "departmentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureEntity> lectures;
}