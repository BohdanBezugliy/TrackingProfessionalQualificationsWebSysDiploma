package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "disciplines")
public class DisciplineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id")
    private Long disciplineId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "discipline_name")
    private String disciplineName;

    @ManyToMany(mappedBy = "disciplines")
    private java.util.List<UpskillEventEntity> upskillEvents;
}