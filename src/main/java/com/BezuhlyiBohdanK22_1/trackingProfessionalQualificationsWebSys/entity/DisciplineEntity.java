package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Сутність, що представляє навчальну дисципліну.
 * Відображається на таблицю "disciplines" у базі даних.
 */
@Getter
@Setter
@Entity
@Table(name = "disciplines")
public class DisciplineEntity {
    /**
     * Унікальний ідентифікатор дисципліни.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id")
    private Long disciplineId;

    /**
     * Назва навчальної дисципліни.
     */
    @NotBlank
    @Size(max = 255)
    @Column(name = "discipline_name")
    private String disciplineName;

    /**
     * Перелік заходів підвищення кваліфікації, що стосуються цієї дисципліни.
     * Зв'язок багато-до-багатьох із {@link UpskillEventEntity}.
     */
    @ManyToMany(mappedBy = "disciplines")
    private java.util.List<UpskillEventEntity> upskillEvents;
}