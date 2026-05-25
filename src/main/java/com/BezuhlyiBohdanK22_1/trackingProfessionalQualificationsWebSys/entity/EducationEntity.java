package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Getter
@Setter
public class EducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Long educationId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private LectureEntity lectureEntity;

    @NotNull
    @PastOrPresent
    @Column(name = "ending_date")
    private LocalDate endingDate;

    @NotBlank
    @Column(name = "specialization")
    @Size(max = 255)
    private String specialization;

    @NotBlank
    @Column(name = "institution_name")
    @Size(max = 255)
    private String institutionName;
}
