package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "upskill_events")
@Getter
@Setter
public class UpskillEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upskill_event_id")
    private Long upskillEventId;

    @NotBlank @Size(max = 255)
    @Column(name = "institution_name")
    private String institutionName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private DocumentEntity documentEntity;

    @NotBlank @Size(max = 500)
    @Column(name = "topic")
    private String topic;

    @NotNull @PastOrPresent
    @Column(name = "date_received")
    private LocalDate dateReceived;

    @PositiveOrZero
    @Column(name = "ects_credits")
    private BigDecimal ectsCredits;

    @PositiveOrZero
    @Column(name = "hours")
    private Integer hours;

    @NotNull
    @Column(name = "date_begin")
    private LocalDate dateBegin;

    @NotNull
    @Column(name = "date_end")
    private LocalDate dateEnd;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;

    @ManyToMany
    @JoinTable(
            name = "disciplines_upskill_events",
            joinColumns = @JoinColumn(name = "upskill_event_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<DisciplineEntity> disciplines;

    @AssertTrue()
    private boolean isDateRangeValid() {
        if (dateBegin == null || dateEnd == null || dateReceived == null) return true;
        return !dateEnd.isBefore(dateBegin) && !dateReceived.isBefore(dateEnd);
    }

    @Column(name = "document_number")
    @Size(max = 100)
    private String documentNumber;

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
