package com.BezuhlyiBohdanK22_1.qualitrack.repository;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LectureRepository extends JpaRepository<LectureEntity, Long> {

    @Query("SELECT l FROM LectureEntity l WHERE " +
           "(:keywordPattern IS NULL OR LOWER(l.firstName) LIKE :keywordPattern " +
           "OR LOWER(l.lastName) LIKE :keywordPattern " +
           "OR LOWER(l.middleName) LIKE :keywordPattern " +
           "OR LOWER(l.userEntity.email) LIKE :keywordPattern) " +
           "AND (:facultyId IS NULL OR l.departmentEntity.facultyEntity.facultyId = :facultyId) " +
           "AND (:departmentId IS NULL OR l.departmentEntity.departmentId = :departmentId)")
    List<LectureEntity> searchLecturers(@Param("keywordPattern") String keywordPattern, 
                                        @Param("facultyId") Long facultyId, 
                                        @Param("departmentId") Long departmentId);
}
