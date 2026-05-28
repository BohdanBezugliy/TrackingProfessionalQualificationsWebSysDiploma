package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DisciplineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для доступу до даних дисциплін.
 * Забезпечує виконання базових CRUD операцій для сутності {@link DisciplineEntity}.
 */
@Repository
public interface DisciplineRepository extends JpaRepository<DisciplineEntity, Long> {
}
