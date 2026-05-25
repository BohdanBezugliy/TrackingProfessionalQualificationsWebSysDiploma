package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import java.util.List;

public interface IFacultyService {
    List<FacultyEntity> findAll();
    FacultyEntity findById(Long id);
    FacultyEntity save(FacultyEntity facultyEntity);
    void deleteById(Long id);
}
