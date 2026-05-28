package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторій для доступу до даних документів.
 * Забезпечує виконання базових CRUD операцій для сутності {@link DocumentEntity}.
 */
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
}
