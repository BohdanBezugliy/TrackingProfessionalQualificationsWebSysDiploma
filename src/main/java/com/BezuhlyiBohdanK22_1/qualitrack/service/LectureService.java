package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.exception.LectureNotFoundException;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService implements ILectureService {
    private final LectureRepository lectureRepository;

    @Transactional
    public void saveLecturer(LectureDto dto) {
        // 1. Створюємо User
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("LECTURER");

        // 2. Створюємо Lecturer
        Lecturer lecturer = new Lecturer();
        lecturer.setFirstName(dto.getFirstName());
        lecturer.setLastName(dto.getLastName());
        lecturer.setAcademicDegree(dto.getAcademicDegree());
        lecturer.setHireDate(dto.getHireDate());
        lecturer.setUser(user);

        // Прив'язка до кафедри
        Department dept = deptRepo.findById(dto.getDepartmentId()).orElseThrow();
        lecturer.setDepartment(dept);

        // 3. Додаємо всі освіти зі списку
        for (EducationDto eduDto : dto.getEducations()) {
            Education edu = new Education();
            edu.setInstitutionName(eduDto.getInstitutionName());
            edu.setSpecialization(eduDto.getSpecialization());
            edu.setEndingDate(eduDto.getEndingDate());
            lecturer.addEducation(edu); // метод додає і встановлює зворотній зв'язок
        }

        lecturerRepo.save(lecturer);
    }

    @Override
    public LectureEntity findByLectureId(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
    }

    @Override
    public List<LectureEntity> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public void save(LectureEntity lectureEntity) {

    }

    @Override
    public void delete(LectureEntity lectureEntity) {

    }

    @Override
    public List<LectureEntity> findAllByLectureName(String lectureName) {
        return List.of();
    }

    @Override
    public List<LectureEntity> findAllByFacultyName(String facultyName) {
        return List.of();
    }

    @Override
    public List<LectureEntity> findAllByDepartmentName(String departmentName) {
        return List.of();
    }

    @Override
    public List<LectureEntity> findAllByFacultyNameAndDepartmentName(String facultyName, String departmentName) {
        return List.of();
    }

    @Override
    public BigDecimal countHoursByLectureIdForYear(Long lectureId, Integer year) {
        return null;
    }

    @Override
    public BigDecimal countCreditsByLectureIdForYear(Long lectureId, Integer year) {
        return null;
    }
}
