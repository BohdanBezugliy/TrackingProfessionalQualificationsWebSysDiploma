package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "scientific_degree")
@Getter
@Setter
public class ScientificDegreeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_scientific_degree_id")
    private Long lectureScientificDegreeId;

    @NotBlank(message = "Назва ступеня обов'язкова")
    @Column(name = "scientific_degree_name")
    private String scientificDegreeName;

    @NotBlank(message = "Номер диплома обов'язковий")
    @Column(name = "diploma_code")
    private String diplomaCode;

    @NotNull
    @Column(name = "received_date")
    @PastOrPresent
    private LocalDate receivedDate;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private LectureEntity lectureEntity;
}
