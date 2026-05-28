package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) для реєстрації та створення профілю викладача.
 * Містить особисту інформацію, дані про освіту, наукові ступені, 
 * а також прив'язку до кафедри та факультету.
 *
 * @param userRegistrationDto Дані для реєстрації облікового запису користувача (email, пароль, роль)
 * @param firstName Ім'я викладача
 * @param middleName По батькові викладача
 * @param lastName Прізвище викладача
 * @param academicDegree Науковий ступінь викладача (наприклад, кандидат наук, доктор наук)
 * @param academicRank Вчене звання викладача (наприклад, доцент, професор)
 * @param hireDate Дата прийняття на роботу
 * @param departmentId Ідентифікатор кафедри, до якої належить викладач
 * @param facultyId Ідентифікатор факультету, до якого належить кафедра
 * @param educations Список записів про отриману освіту викладачем
 */
public record LectureDto(
        UserRegistrationDto userRegistrationDto,

        String firstName,
        String middleName,
        String lastName,
        String academicDegree,
        String academicRank,
        LocalDate hireDate,

        Long departmentId,
        Long facultyId,
        List<EducationDto> educations
) {}
