package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "academic_ranks")
@Getter
@Setter
public class AcademicRankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_academic_rank_id")
    private Long lectureAcademicRankId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;

    @NotBlank @Size(max = 255)
    @Column(name = "certificate_code")
    private String certificateCode;

    @NotNull
    @PastOrPresent
    @Column(name = "received_date")
    private LocalDate receivedDate;

    @NotBlank
    @Size(max = 255)
    @Column(name = "academic_rank_name")
    private String academicRankName;
}
