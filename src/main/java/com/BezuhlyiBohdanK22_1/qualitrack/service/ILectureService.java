package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ILectureService {
    LectureEntity findByLectureId(Long lectureId);
    List<LectureEntity> findAll();
    void save(LectureEntity lectureEntity);
    void delete(LectureEntity lectureEntity);
    List<LectureEntity> findAllByLectureName(String lectureName);
    List<LectureEntity> findAllByFacultyName(String facultyName);
    List<LectureEntity> findAllByDepartmentName(String departmentName);
    List<LectureEntity> findAllByFacultyNameAndDepartmentName(String facultyName, String departmentName);
    BigDecimal countHoursByLectureIdForYear(Long lectureId, Integer year);
    BigDecimal countCreditsByLectureIdForYear(Long lectureId, Integer year);
}
