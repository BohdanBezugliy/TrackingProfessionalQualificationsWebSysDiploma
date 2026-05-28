package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для доступу до даних кафедр.
 * Забезпечує виконання базових CRUD операцій для сутності {@link DepartmentEntity}.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
}
