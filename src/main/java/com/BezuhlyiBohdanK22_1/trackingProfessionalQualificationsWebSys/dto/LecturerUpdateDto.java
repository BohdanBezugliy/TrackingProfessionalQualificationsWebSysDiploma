package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) для оновлення даних існуючого профілю викладача.
 * Містить поля, які можуть бути змінені при редагуванні інформації про викладача.
 *
 * @param firstName Ім'я викладача
 * @param middleName По батькові викладача
 * @param lastName Прізвище викладача
 * @param email Контактна електронна адреса викладача
 * @param academicDegree Науковий ступінь викладача
 * @param academicRank Вчене звання викладача
 * @param hireDate Дата прийняття на роботу
 * @param departmentId Ідентифікатор нової або поточної кафедри викладача
 * @param educations Оновлений список записів про освіту
 */
public record LecturerUpdateDto(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String academicDegree,
        String academicRank,
        LocalDate hireDate,
        Long departmentId,
        List<EducationDto> educations
) {}
