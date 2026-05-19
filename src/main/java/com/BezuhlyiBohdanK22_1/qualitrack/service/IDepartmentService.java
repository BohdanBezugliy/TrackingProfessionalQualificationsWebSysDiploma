package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.DepartmentEntity;
import java.util.List;

public interface IDepartmentService {
    List<DepartmentEntity> findAll();
    DepartmentEntity findById(Long id);
    DepartmentEntity save(DepartmentEntity departmentEntity);
}
