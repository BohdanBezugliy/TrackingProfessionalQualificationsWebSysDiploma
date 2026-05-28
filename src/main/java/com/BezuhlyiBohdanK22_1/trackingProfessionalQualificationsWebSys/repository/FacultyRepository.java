package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для доступу до даних факультетів.
 * Забезпечує виконання базових CRUD операцій для сутності {@link FacultyEntity}.
 */
@Repository
public interface FacultyRepository extends JpaRepository<FacultyEntity, Long> {
}
