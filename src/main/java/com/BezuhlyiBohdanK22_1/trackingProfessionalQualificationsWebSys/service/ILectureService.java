package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LectureDto;

import java.util.List;

public interface ILectureService {
    LectureEntity findByLectureId(Long lectureId);
    void saveLecturer(LectureDto dto);
    org.springframework.data.domain.Page<LectureEntity> findAll(org.springframework.data.domain.Pageable pageable);
    void save(LectureEntity lectureEntity);

    void deleteLecturerById(Long lectureId);
    void updateProfile(Long lectureId, com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto dto);
    void updatePassword(Long lectureId, String newPassword);

    org.springframework.data.domain.Page<LectureEntity> searchLecturers(String keyword, Long facultyId, Long departmentId, org.springframework.data.domain.Pageable pageable);

}
