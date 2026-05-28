package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UpskillEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для доступу до даних заходів підвищення кваліфікації.
 * Забезпечує виконання базових CRUD операцій та спеціалізованих запитів для сутності {@link UpskillEventEntity}.
 */
@Repository
public interface UpskillEventRepository extends JpaRepository<UpskillEventEntity, Long> {
    /**
     * Знаходить всі заходи підвищення кваліфікації для конкретного викладача.
     *
     * @param lectureId ідентифікатор викладача.
     * @return список заходів підвищення кваліфікації, пов'язаних із вказаним викладачем.
     */
    List<UpskillEventEntity> findAllByLectureEntity_LectureId(Long lectureId);
}
