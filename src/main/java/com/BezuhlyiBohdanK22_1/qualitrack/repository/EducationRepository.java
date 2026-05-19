package com.BezuhlyiBohdanK22_1.qualitrack.repository;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    void deleteByLectureEntity(LectureEntity lectureEntity);
}
