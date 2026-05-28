package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;

/**
 * Репозиторій для доступу до даних про освіту викладачів.
 * Забезпечує виконання базових CRUD операцій та спеціалізованих запитів для сутності {@link EducationEntity}.
 */
@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    /**
     * Видаляє всі записи про освіту, пов'язані з конкретним викладачем.
     *
     * @param lectureEntity сутність викладача, для якого потрібно видалити дані про освіту.
     */
    void deleteByLectureEntity(LectureEntity lectureEntity);
}
