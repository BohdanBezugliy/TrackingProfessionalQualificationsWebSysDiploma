package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "lectures")
@Entity
@Getter
@Setter
public class LectureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Integer lectureId;

    @NotBlank
    @Size(max = 120)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 120)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 120)
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "hire_date")
    @PastOrPresent
    private LocalDate hireDate;

    @NotBlank
    @Size(max = 255)
    private String position;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity departmentEntity;
}
