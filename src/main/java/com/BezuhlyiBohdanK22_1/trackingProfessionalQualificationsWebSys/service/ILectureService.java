package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LectureDto;

import java.util.List;

/**
 * Інтерфейс сервісу для управління викладачами (лекторами).
 * Надає методи для створення, оновлення, видалення та пошуку викладачів у системі.
 */
public interface ILectureService {
    
    /**
     * Знаходить викладача за його ідентифікатором.
     * 
     * @param lectureId ідентифікатор викладача
     * @return сутність викладача {@link LectureEntity}
     * @throws com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception.LectureNotFoundException якщо викладача не знайдено
     */
    LectureEntity findByLectureId(Long lectureId);

    /**
     * Створює нового викладача разом з його обліковим записом та інформацією про освіту.
     * 
     * @param dto об'єкт {@link LectureDto} з даними нового викладача
     */
    void saveLecturer(LectureDto dto);

    /**
     * Отримує список усіх викладачів з підтримкою пагінації.
     * 
     * @param pageable параметри пагінації та сортування
     * @return сторінка з об'єктами {@link LectureEntity}
     */
    org.springframework.data.domain.Page<LectureEntity> findAll(org.springframework.data.domain.Pageable pageable);

    /**
     * Зберігає або оновлює інформацію про існуючого викладача.
     * 
     * @param lectureEntity сутність викладача, яку потрібно зберегти
     */
    void save(LectureEntity lectureEntity);

    /**
     * Видаляє викладача за його ідентифікатором, включаючи пов'язаний обліковий запис та дані про освіту.
     * 
     * @param lectureId ідентифікатор викладача для видалення
     */
    void deleteLecturerById(Long lectureId);

    /**
     * Оновлює профіль викладача, включаючи його персональні дані, кафедру та освіту.
     * 
     * @param lectureId ідентифікатор викладача, чий профіль оновлюється
     * @param dto об'єкт {@link com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto} з новими даними
     */
    void updateProfile(Long lectureId, com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto dto);

    /**
     * Оновлює пароль облікового запису викладача.
     * 
     * @param lectureId ідентифікатор викладача
     * @param newPassword новий пароль
     */
    void updatePassword(Long lectureId, String newPassword);

    /**
     * Виконує пошук викладачів за ключовим словом, факультетом та кафедрою з підтримкою пагінації.
     * 
     * @param keyword ключове слово для пошуку (за ПІБ або email)
     * @param facultyId ідентифікатор факультету для фільтрації (опціонально)
     * @param departmentId ідентифікатор кафедри для фільтрації (опціонально)
     * @param pageable параметри пагінації та сортування
     * @return сторінка з об'єктами {@link LectureEntity}, що відповідають критеріям пошуку
     */
    org.springframework.data.domain.Page<LectureEntity> searchLecturers(String keyword, Long facultyId, Long departmentId, org.springframework.data.domain.Pageable pageable);

}
