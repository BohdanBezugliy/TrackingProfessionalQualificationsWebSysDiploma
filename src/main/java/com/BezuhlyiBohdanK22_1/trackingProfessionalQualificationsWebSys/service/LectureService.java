package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.EducationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.EducationEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.LectureNotFoundException;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.UserAlreadyExistsException;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.EducationRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.LectureRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Реалізація сервісу для управління викладачами (лекторами).
 * Забезпечує бізнес-логіку для створення викладачів, оновлення їх профілів, 
 * управління обліковими записами (паролями) та пошуку.
 */
@Service
@RequiredArgsConstructor
public class LectureService implements ILectureService {
    
    /** Репозиторій для роботи з сутностями викладачів. */
    private final LectureRepository lectureRepository;
    
    /** Репозиторій для роботи з користувачами системи. */
    private final UserRepository userRepository;
    
    /** Репозиторій для роботи з кафедрами. */
    private final DepartmentRepository departmentRepository;
    
    /** Репозиторій для роботи з даними про освіту викладачів. */
    private final EducationRepository educationRepository;
    
    /** Компонент для шифрування паролів. */
    private final PasswordEncoder passwordEncoder;

    /**
     * Створює нового викладача, його обліковий запис та зберігає інформацію про його освіту.
     * Перевіряє унікальність email перед створенням.
     * 
     * @param dto об'єкт {@link LectureDto}, що містить усі необхідні дані для реєстрації
     * @throws UserAlreadyExistsException якщо користувач з таким email вже існує
     * @throws RuntimeException якщо вказану кафедру не знайдено
     */
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
                edu.setInstitutionName(eduDto.getInstitutionName());
                edu.setSpecialization(eduDto.getSpecialization());
                edu.setEndingDate(eduDto.getEndingDate());
                edu.setLectureEntity(lecturer);
                educationRepository.save(edu);
            }
        }
    }

    /**
     * Знаходить викладача за його унікальним ідентифікатором.
     * 
     * @param lectureId ідентифікатор викладача
     * @return знайдена сутність {@link LectureEntity}
     * @throws LectureNotFoundException якщо викладача не знайдено
     */
    @Override
    public LectureEntity findByLectureId(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
    }

    /**
     * Отримує список усіх викладачів з підтримкою пагінації.
     * 
     * @param pageable налаштування пагінації (номер сторінки, розмір, сортування)
     * @return сторінка з викладачами
     */
    @Override
    public org.springframework.data.domain.Page<LectureEntity> findAll(org.springframework.data.domain.Pageable pageable) {
        return lectureRepository.findAll(pageable);
    }

    /**
     * Зберігає або оновлює існуючого викладача напряму.
     * 
     * @param lectureEntity сутність викладача для збереження
     */
    @Override
    public void save(LectureEntity lectureEntity) {
        lectureRepository.save(lectureEntity);
    }

    /**
     * Видаляє викладача, його обліковий запис та всі пов'язані дані про освіту.
     * 
     * @param lectureId ідентифікатор викладача, якого потрібно видалити
     * @throws LectureNotFoundException якщо викладача не знайдено
     */
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

    /**
     * Оновлює профіль викладача (персональні дані, кафедру, email, дані про освіту).
     * Якщо email змінюється, перевіряється його унікальність.
     * Дані про освіту повністю замінюються новими.
     * 
     * @param lectureId ідентифікатор викладача
     * @param dto DTO з новими даними профілю
     * @throws LectureNotFoundException якщо викладача не знайдено
     * @throws UserAlreadyExistsException якщо новий email вже використовується іншим користувачем
     */
    @Override
    @Transactional
    public void updateProfile(Long lectureId, LecturerDto dto) {
        LectureEntity lecturer = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new LectureNotFoundException("Lecture not found!"));
        
        // Update user email if changed
        UserEntity user = lecturer.getUserEntity();
        if (user != null && dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistsException("Користувач з таким email вже існує!");
            }
            user.setEmail(dto.getEmail());
            userRepository.save(user);
        }

        lecturer.setFirstName(dto.getFirstName());
        lecturer.setLastName(dto.getLastName());
        lecturer.setMiddleName(dto.getMiddleName());
        lecturer.setAcademicDegree(dto.getAcademicDegree());
        lecturer.setAcademicRank(dto.getAcademicRank());
        
        if (dto.getHireDate() != null) {
            lecturer.setHireDate(dto.getHireDate());
        }
        
        if (dto.getDepartmentId() != null) {
            DepartmentEntity dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            lecturer.setDepartmentEntity(dept);
        }
        
        // Handle education updates
        if (dto.getEducations() != null) {
            // Remove existing educations
            if (lecturer.getEducations() != null) {
                lecturer.getEducations().clear();
            } else {
                lecturer.setEducations(new ArrayList<>());
            }
            
            for (com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.EducationDto edDto : dto.getEducations()) {
                if (edDto.getInstitutionName() != null && !edDto.getInstitutionName().isEmpty()) {
                    EducationEntity edEntity = new EducationEntity();
                    edEntity.setInstitutionName(edDto.getInstitutionName());
                    edEntity.setSpecialization(edDto.getSpecialization());
                    edEntity.setEndingDate(edDto.getEndingDate());
                    edEntity.setLectureEntity(lecturer);
                    lecturer.getEducations().add(edEntity);
                }
            }
        }
        
        lectureRepository.save(lecturer);
    }

    /**
     * Оновлює пароль для облікового запису викладача.
     * Пароль хешується перед збереженням.
     * 
     * @param lectureId ідентифікатор викладача
     * @param newPassword новий пароль у відкритому вигляді
     * @throws LectureNotFoundException якщо викладача не знайдено
     */
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

    /**
     * Виконує пошук викладачів за заданим ключовим словом та/або ідентифікаторами факультету і кафедри.
     * Ключове слово застосовується як маска "%keyword%" до відповідних полів.
     * 
     * @param keyword рядок для пошуку 
     * @param facultyId ідентифікатор факультету (якщо null — ігнорується)
     * @param departmentId ідентифікатор кафедри (якщо null — ігнорується)
     * @param pageable параметри пагінації та сортування
     * @return сторінка з результатами пошуку
     */
    @Override
    public org.springframework.data.domain.Page<LectureEntity> searchLecturers(String keyword, Long facultyId, Long departmentId, org.springframework.data.domain.Pageable pageable) {
        String keywordPattern = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            keywordPattern = "%" + keyword.trim().toLowerCase() + "%";
        }
        return lectureRepository.searchLecturers(keywordPattern, facultyId, departmentId, pageable);
    }

}
