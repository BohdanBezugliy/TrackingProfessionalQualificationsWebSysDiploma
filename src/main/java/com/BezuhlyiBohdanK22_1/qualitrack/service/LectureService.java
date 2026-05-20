package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.EducationDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.EducationEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.exception.LectureNotFoundException;
import com.BezuhlyiBohdanK22_1.qualitrack.exception.UserAlreadyExistsException;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.EducationRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.LectureRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService implements ILectureService {
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EducationRepository educationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveLecturer(LectureDto dto) {
        String email = dto.userRegistrationDto().email();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Користувач з email " + email + " вже існує.");
        }

        // 1. Створюємо User
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(dto.userRegistrationDto().password()));
        user.setUserRole(dto.userRegistrationDto().role());
        
        userRepository.save(user);

        // 2. Створюємо Lecturer
        LectureEntity lecturer = new LectureEntity();
        lecturer.setFirstName(dto.firstName());
        lecturer.setLastName(dto.lastName());
        lecturer.setMiddleName(dto.middleName());
        lecturer.setAcademicDegree(dto.academicDegree());
        lecturer.setAcademicRank(dto.academicRank());
        lecturer.setHireDate(dto.hireDate());
        lecturer.setUserEntity(user);

        // Прив'язка до кафедри
        DepartmentEntity dept = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        lecturer.setDepartmentEntity(dept);
        
        lectureRepository.save(lecturer);

        // 3. Додаємо всі освіти зі списку
        if (dto.educations() != null) {
            for (EducationDto eduDto : dto.educations()) {
                EducationEntity edu = new EducationEntity();
                edu.setInstitutionName(eduDto.institutionName());
                edu.setSpecialization(eduDto.specialization());
                edu.setEndingDate(eduDto.endingDate());
                edu.setLectureEntity(lecturer);
                educationRepository.save(edu);
            }
        }
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
        lectureRepository.save(lectureEntity);
    }

    @Override
    public void delete(LectureEntity lectureEntity) {
        lectureRepository.delete(lectureEntity);
    }

    @Override
    @Transactional
    public void deleteLecturerById(Long lectureId) {
        LectureEntity lecturer = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
        
        // 1. Delete educations
        educationRepository.deleteByLectureEntity(lecturer);
        
        // 2. Remember UserEntity
        UserEntity user = lecturer.getUserEntity();
        
        // 3. Delete lecturer
        lectureRepository.delete(lecturer);
        
        // 4. Delete user
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional
    public void updateProfile(Long lectureId, LectureDto dto) {
        LectureEntity lecturer = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
        
        lecturer.setFirstName(dto.firstName());
        lecturer.setLastName(dto.lastName());
        lecturer.setMiddleName(dto.middleName());
        lecturer.setAcademicDegree(dto.academicDegree());
        lecturer.setAcademicRank(dto.academicRank());
        
        if (dto.departmentId() != null) {
            DepartmentEntity dept = departmentRepository.findById(dto.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            lecturer.setDepartmentEntity(dept);
        }
        
        lectureRepository.save(lecturer);
    }

    @Override
    @Transactional
    public void updatePassword(Long lectureId, String newPassword) {
        LectureEntity lecturer = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
        
        UserEntity user = lecturer.getUserEntity();
        if (user != null) {
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
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
    public List<LectureEntity> searchLecturers(String keyword, Long facultyId, Long departmentId) {
        String keywordPattern = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            keywordPattern = "%" + keyword.trim().toLowerCase() + "%";
        }
        return lectureRepository.searchLecturers(keywordPattern, facultyId, departmentId);
    }

    @Override
    public BigDecimal countHoursByLectureIdForYear(Long lectureId, Integer year) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal countCreditsByLectureIdForYear(Long lectureId, Integer year) {
        return BigDecimal.ZERO;
    }
}
