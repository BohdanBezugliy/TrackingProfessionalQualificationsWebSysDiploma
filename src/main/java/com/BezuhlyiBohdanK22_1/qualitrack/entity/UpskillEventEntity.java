package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "upskill_event")
@Getter
@Setter
public class UpskillEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upskill_event_id")
    private Integer upskillEventId;

    @NotBlank @Size(max = 255)
    @Column(name = "institution_name")
    private String institutionName;

    @OneToOne(fetch = FetchType.LAZY)
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
            name = "discipline_upskill_event",
            joinColumns = @JoinColumn(name = "upskill_event_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private List<DisciplineEntity> disciplines;

    @AssertTrue()
    private boolean isDateRangeValid() {
        return dateEnd.isAfter(dateBegin);
    }
}
