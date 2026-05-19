package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.FacultyEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService implements IFacultyService {

    private final FacultyRepository facultyRepository;

    @Override
    public List<FacultyEntity> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public FacultyEntity findById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    @Override
    public FacultyEntity save(FacultyEntity facultyEntity) {
        return facultyRepository.save(facultyEntity);
    }
}
