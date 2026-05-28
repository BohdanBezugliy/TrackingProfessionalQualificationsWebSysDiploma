package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Сутність, що представляє захід підвищення кваліфікації.
 * Зберігає детальну інформацію про пройдене навчання (тема, заклад, дати, кредити).
 * Відображається на таблицю "upskill_events" у базі даних.
 */
@Entity
@Table(name = "upskill_events")
@Getter
@Setter
public class UpskillEventEntity {
    /**
     * Унікальний ідентифікатор заходу підвищення кваліфікації.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upskill_event_id")
    private Long upskillEventId;

    /**
     * Назва закладу або установи, де проводився захід.
     */
    @NotBlank @Size(max = 255)
    @Column(name = "institution_name")
    private String institutionName;

    /**
     * Документ (наприклад, сертифікат), що підтверджує проходження.
     * Зв'язок один-до-одного із {@link DocumentEntity}.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private DocumentEntity documentEntity;

    /**
     * Тема або напрямок заходу.
     */
    @NotBlank @Size(max = 500)
    @Column(name = "topic")
    private String topic;

    /**
     * Дата отримання підтверджуючого документа.
     */
    @NotNull @PastOrPresent
    @Column(name = "date_received")
    private LocalDate dateReceived;

    /**
     * Кількість отриманих кредитів ЄКТС (ECTS).
     */
    @PositiveOrZero
    @Column(name = "ects_credits")
    private BigDecimal ectsCredits;

    /**
     * Загальна тривалість заходу в годинах.
     */
    @PositiveOrZero
    @Column(name = "hours")
    private Integer hours;

    /**
     * Дата початку проходження заходу підвищення кваліфікації.
     */
    @NotNull
    @Column(name = "date_begin")
    private LocalDate dateBegin;

    /**
     * Дата завершення проходження заходу підвищення кваліфікації.
     */
    @NotNull
    @Column(name = "date_end")
    private LocalDate dateEnd;

    /**
     * Викладач, який пройшов цей захід підвищення кваліфікації.
     * Зв'язок багато-до-одного із {@link LectureEntity}.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;

    /**
     * Перелік дисциплін, яких стосується цей захід підвищення кваліфікації.
     * Зв'язок багато-до-багатьох із {@link DisciplineEntity}.
     */
    @ManyToMany
    @JoinTable(
            name = "disciplines_upskill_events",
            joinColumns = @JoinColumn(name = "upskill_event_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<DisciplineEntity> disciplines;

    /**
     * Перевіряє валідність введених дат.
     * Дата закінчення не може бути раніше дати початку,
     * а дата отримання документа не може бути раніше дати закінчення заходу.
     *
     * @return true, якщо дати логічно коректні або відсутні.
     */
    @AssertTrue()
    private boolean isDateRangeValid() {
        if (dateBegin == null || dateEnd == null || dateReceived == null) return true;
        return !dateEnd.isBefore(dateBegin) && !dateReceived.isBefore(dateEnd);
    }

    /**
     * Реєстраційний номер підтверджуючого документа.
     */
    @Column(name = "document_number")
    @Size(max = 100)
    private String documentNumber;

    /**
     * Автоматично розраховує кредити ЄКТС (ECTS) на основі кількості годин або навпаки
     * перед збереженням чи оновленням запису в базі даних.
     * Використовує співвідношення: 1 кредит ЄКТС = 30 годин.
     */
    @PrePersist
    @PreUpdate
    public void calculateHoursAndCredits() {
        if (hours != null && (ectsCredits == null || ectsCredits.compareTo(BigDecimal.ZERO) == 0)) {
            ectsCredits = BigDecimal.valueOf(hours).divide(BigDecimal.valueOf(30), 2, java.math.RoundingMode.HALF_UP);
        } else if (ectsCredits != null && (hours == null || hours == 0)) {
            hours = ectsCredits.multiply(BigDecimal.valueOf(30)).intValue();
        }
    }
}
