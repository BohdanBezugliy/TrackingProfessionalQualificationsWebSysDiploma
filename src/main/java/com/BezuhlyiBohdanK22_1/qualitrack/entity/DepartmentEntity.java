package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @NotBlank()
    @Column(name = "department_name")
    @Size(max = 255)
    private String departmentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private FacultyEntity facultyEntity;

    @OneToMany(mappedBy = "departmentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureEntity> lectures;
}