package com.BezuhlyiBohdanK22_1.qualitrack.request;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LectureRequest {
    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate hireDate;

    private String position;

    private UserEntity userEntity;

    private DepartmentEntity departmentEntity;

}
