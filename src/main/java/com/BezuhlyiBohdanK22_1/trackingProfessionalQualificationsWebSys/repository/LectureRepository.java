package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<LectureEntity, Long> {

    @Query("SELECT l FROM LectureEntity l WHERE l.userEntity.email = :email")
    Optional<LectureEntity> findByUserEmail(@Param("email") String email);

    @Query("SELECT l FROM LectureEntity l WHERE " +
           "(:keywordPattern IS NULL OR LOWER(l.firstName) LIKE :keywordPattern " +
           "OR LOWER(l.lastName) LIKE :keywordPattern " +
           "OR LOWER(l.middleName) LIKE :keywordPattern " +
           "OR LOWER(l.userEntity.email) LIKE :keywordPattern) " +
           "AND (:facultyId IS NULL OR l.departmentEntity.facultyEntity.facultyId = :facultyId) " +
           "AND (:departmentId IS NULL OR l.departmentEntity.departmentId = :departmentId)")
    org.springframework.data.domain.Page<LectureEntity> searchLecturers(@Param("keywordPattern") String keywordPattern, 
                                        @Param("facultyId") Long facultyId, 
                                        @Param("departmentId") Long departmentId,
                                        org.springframework.data.domain.Pageable pageable);
}
