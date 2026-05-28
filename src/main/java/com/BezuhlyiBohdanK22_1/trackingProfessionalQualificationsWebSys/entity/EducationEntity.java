package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Сутність, що представляє відомості про вищу освіту викладача.
 * Відображається на таблицю "educations" у базі даних.
 */
@Entity
@Table(name = "educations")
@Getter
@Setter
public class EducationEntity {
    /**
     * Унікальний ідентифікатор запису про освіту.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long educationId;

    /**
     * Викладач, якому належать ці відомості про освіту.
     * Зв'язок багато-до-одного із {@link LectureEntity}.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private LectureEntity lectureEntity;

    /**
     * Дата закінчення навчання.
     */
    @NotNull
    @PastOrPresent
    @Column(name = "ending_date")
    private LocalDate endingDate;

    /**
     * Спеціальність, здобута за результатами навчання.
     */
    @NotBlank
    @Column(name = "specialization")
    @Size(max = 255)
    private String specialization;

    /**
     * Назва навчального закладу, де було здобуто освіту.
     */
    @NotBlank
    @Column(name = "institution_name")
    @Size(max = 255)
    private String institutionName;
}
