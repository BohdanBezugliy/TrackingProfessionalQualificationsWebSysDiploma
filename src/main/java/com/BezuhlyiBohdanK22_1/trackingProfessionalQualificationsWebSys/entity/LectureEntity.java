package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Сутність, що представляє викладача (лектора) у системі.
 * Зберігає особисті дані викладача, його посаду, науковий ступінь
 * та зв'язки з кафедрою, заходами підвищення кваліфікації та профілем користувача.
 * Відображається на таблицю "lectures" у базі даних.
 */
@Table(name = "lectures")
@Entity
@Getter
@Setter
public class LectureEntity {
    /**
     * Унікальний ідентифікатор викладача.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    /**
     * Ім'я викладача.
     */
    @NotBlank
    @Size(max = 120)
    @Column(name = "first_name")
    private String firstName;

    /**
     * Прізвище викладача.
     */
    @NotBlank
    @Size(max = 120)
    @Column(name = "last_name")
    private String lastName;

    /**
     * По батькові викладача.
     */
    @Size(max = 120)
    @Column(name = "middle_name")
    private String middleName;

    /**
     * Дата прийняття на роботу.
     */
    @NotNull
    @Column(name = "hire_date")
    @PastOrPresent
    private LocalDate hireDate;

    /**
     * Науковий ступінь викладача (наприклад, "Кандидат наук").
     */
    @Size(max = 150)
    @Column(name = "academic_degree")
    private String academicDegree;

    /**
     * Вчене звання викладача (наприклад, "Доцент").
     */
    @Size(max = 150)
    @Column(name = "academic_rank")
    private String academicRank;

    /**
     * Обліковий запис користувача, пов'язаний з цим викладачем.
     * Зв'язок один-до-одного із {@link UserEntity}.
     */
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    /**
     * Кафедра, до якої належить викладач.
     * Зв'язок багато-до-одного із {@link DepartmentEntity}.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity departmentEntity;

    /**
     * Список відомостей про освіту викладача.
     * Зв'язок один-до-багатьох із {@link EducationEntity}.
     */
    @OneToMany(mappedBy = "lectureEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationEntity> educations;

    /**
     * Список заходів підвищення кваліфікації, пройдених викладачем.
     * Зв'язок один-до-багатьох із {@link UpskillEventEntity}.
     */
    @OneToMany(mappedBy = "lectureEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpskillEventEntity> upskillEvents;

    /**
     * Обчислює загальну кількість годин, витрачених на підвищення кваліфікації.
     * 
     * @return загальна кількість годин або 0, якщо список заходів порожній або відсутній.
     */
    @Transient
    public Integer getTotalHours() {
        if (upskillEvents == null) return 0;
        return upskillEvents.stream()
                .filter(e -> e.getHours() != null)
                .mapToInt(UpskillEventEntity::getHours)
                .sum();
    }

    /**
     * Обчислює загальну кількість кредитів ЄКТС (ECTS), отриманих за підвищення кваліфікації.
     * 
     * @return загальна кількість кредитів ЄКТС або 0, якщо список заходів порожній або відсутній.
     */
    @Transient
    public java.math.BigDecimal getTotalEctsCredits() {
        if (upskillEvents == null) return java.math.BigDecimal.ZERO;
        return upskillEvents.stream()
                .filter(e -> e.getEctsCredits() != null)
                .map(UpskillEventEntity::getEctsCredits)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}
