package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UpskillEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpskillEventRepository extends JpaRepository<UpskillEventEntity, Long> {
    List<UpskillEventEntity> findAllByLectureEntity_LectureId(Long lectureId);
}
