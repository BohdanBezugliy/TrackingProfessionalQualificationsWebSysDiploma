package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Table(name = "lectures")
@Entity
@Getter
@Setter
public class LectureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

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

    @Size(max = 150)
    @Column(name = "academic_degree")
    private String academicDegree;

    @Size(max = 150)
    @Column(name = "academic_rank")
    private String academicRank;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity departmentEntity;

    @OneToMany(mappedBy = "lectureEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EducationEntity> educations;

    @OneToMany(mappedBy = "lectureEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpskillEventEntity> upskillEvents;

    @Transient
    public Integer getTotalHours() {
        if (upskillEvents == null) return 0;
        return upskillEvents.stream()
                .filter(e -> e.getHours() != null)
                .mapToInt(UpskillEventEntity::getHours)
                .sum();
    }

    @Transient
    public java.math.BigDecimal getTotalEctsCredits() {
        if (upskillEvents == null) return java.math.BigDecimal.ZERO;
        return upskillEvents.stream()
                .filter(e -> e.getEctsCredits() != null)
                .map(UpskillEventEntity::getEctsCredits)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}
