package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class FacultyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long facultyId;

    @NotBlank(message = "Назва факультету обов'язкова")
    @Column(name = "faculty_name")
    @Size(max = 255)
    private String facultyName;
}