package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import java.util.List;

/**
 * Інтерфейс сервісу для управління кафедрами.
 * Визначає контракти для отримання, збереження та видалення інформації про кафедри.
 */
public interface IDepartmentService {
    
    /**
     * Отримує список всіх наявних кафедр у системі.
     * 
     * @return список об'єктів {@link DepartmentEntity}
     */
    List<DepartmentEntity> findAll();

    /**
     * Отримує кафедру за її унікальним ідентифікатором.
     * 
     * @param id ідентифікатор кафедри
     * @return об'єкт {@link DepartmentEntity}, якщо кафедру знайдено, або null
     */
    DepartmentEntity findById(Long id);

    /**
     * Зберігає нову або оновлює існуючу кафедру в базі даних.
     * 
     * @param departmentEntity сутність кафедри, яку потрібно зберегти
     * @return збережений об'єкт {@link DepartmentEntity}
     */
    DepartmentEntity save(DepartmentEntity departmentEntity);

    /**
     * Видаляє кафедру з бази даних за її ідентифікатором.
     * 
     * @param id ідентифікатор кафедри, яку потрібно видалити
     */
    void deleteById(Long id);
}
