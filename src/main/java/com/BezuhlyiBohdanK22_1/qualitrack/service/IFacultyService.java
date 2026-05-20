package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.FacultyEntity;
import java.util.List;

public interface IFacultyService {
    List<FacultyEntity> findAll();
    FacultyEntity findById(Long id);
    FacultyEntity save(FacultyEntity facultyEntity);
    void deleteById(Long id);
}
