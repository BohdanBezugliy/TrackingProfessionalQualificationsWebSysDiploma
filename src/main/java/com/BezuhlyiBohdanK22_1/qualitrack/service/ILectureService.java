package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;

import java.util.List;

public interface ILectureService {
    LectureEntity findByLectureId(Long lectureId);
    void saveLecturer(LectureDto dto);
    List<LectureEntity> findAll();
    void save(LectureEntity lectureEntity);

    void deleteLecturerById(Long lectureId);
    void updateProfile(Long lectureId, com.BezuhlyiBohdanK22_1.qualitrack.dto.LecturerDto dto);
    void updatePassword(Long lectureId, String newPassword);

    List<LectureEntity> searchLecturers(String keyword, Long facultyId, Long departmentId);

}
