package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "lecture_experience")
@Getter
@Setter
public class LectureExperienceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_experience_id")
    private Long lectureExperienceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private LectureEntity lectureEntity;

    @NotBlank
    @Column(name = "institution_name")
    @Size(max = 255)
    private String institutionName;

    @NotNull
    @PastOrPresent
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @PastOrPresent
    @Column(name = "end_date")
    private LocalDate endDate;

    @AssertTrue
    private boolean isYearsValid(){
        return endDate.isAfter(startDate);
    };
}
